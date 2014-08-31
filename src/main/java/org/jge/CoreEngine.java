package org.jge;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.jge.Profiler.ProfileTimer;
import org.jge.cl.GPUProgramResource;
import org.jge.game.Game;
import org.jge.game.Input;
import org.jge.phys.PhysicsEngine;
import org.jge.render.RenderEngine;
import org.jge.render.Texture;
import org.jge.render.shaders.JavaShader;
import org.jge.sound.Music;
import org.jge.sound.SoundEngine;
import org.jge.util.LWJGLHandler;
import org.jge.util.Log;
import org.jge.util.OpenGLUtils;
import org.jge.util.Strings;

import org.jglrxavpok.jlsl.BytecodeDecoder;
import org.jglrxavpok.jlsl.glsl.GLSLEncoder;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.opencl.CL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GLContext;

/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 jglrxavpok
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * @author jglrxavpok
 * 
 */
public final class CoreEngine
{

	private ProfileTimer				  sleepTimer				  = new ProfileTimer();
	private ProfileTimer				  windowUpdateTimer		   = new ProfileTimer();
	private boolean					   running;
	private Window						window;
	private RenderEngine				  renderEngine;
	private SoundEngine				   soundEngine;
	private ClasspathSimpleResourceLoader classResLoader;
	private Game						  game;

	private int						   frame;
	private int						   fps;
	private double						expectedFrameRate		   = 60.0;
	private double						timeBetweenUpdates		  = 1000000000 / expectedFrameRate;
	private final int					 maxUpdatesBeforeRender	  = 2;
	private double						lastUpdateTime			  = System.nanoTime();
	private double						lastRenderTime			  = System.nanoTime();

	// If we are able to get as high as this FPS, don't render again.
	private final double				  TARGET_FPS				  = 60;
	private final double				  TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

	private int						   lastSecondTime			  = (int)(lastUpdateTime / 1000000000);
	private boolean					   paused					  = false;
	private PhysicsEngine				 physEngine;
	private DiskSimpleResourceLoader	  diskResLoader;
	private static CoreEngine			 current;
	private ArrayList<ITickable>		  tickables				   = new ArrayList<ITickable>();

	public CoreEngine(Game game)
	{
		current = this;
		JGEngine.setGame(game.setEngine(this));
		this.game = game;
	}

	public Game getGame()
	{
		return game;
	}

	public boolean isRunning()
	{
		return running;
	}

	public void start(Window window)
	{
		start(window, new String[0]);
	}

	public void start(Window window, String args[])
	{
		new Thread(() ->
		{
			try
			{
				new ThreadReadConsoleInput().start();
				classResLoader = new ClasspathSimpleResourceLoader();
				diskResLoader = new DiskSimpleResourceLoader();
				running = true;
				this.window = window;
				expectedFrameRate = window.getFPSCap();
				timeBetweenUpdates = 1000000000 / expectedFrameRate;
				try
				{
					LWJGLHandler.load(new File(JGEngine.getEngineFolder(), "natives"));
					window.setTitle(game.getGameName());
					window.init();
					String currentParameter = null;
					HashMap<String, String> props = new HashMap<String, String>();
					for(int i = 0; i < args.length; i++ )
					{
						if(args[i].startsWith("-"))
						{
							currentParameter = args[i].substring(1);
						}
						else
						{
							props.put(currentParameter, args[i]);
						}
					}
					if(props.get("debugAll").equals("true"))
					{
						Log.save = true;
						GLSLEncoder.DEBUG = true;
						BytecodeDecoder.DEBUG = true;
						JavaShader.DEBUG_PRINT_GLSL_TRANSLATION = true;
					}
					if(!GLContext.getCapabilities().OpenGL33)
					{
						Log.message("Incompatible OpenGL version: " + OpenGLUtils.getOpenGLVersion());
						Log.message("Must be at least: 3.3.0");
						cleanup();
					}
					soundEngine = new SoundEngine();

					OpenGLUtils.loadCapNames();
					CL.create();
					Log.message("[------OpenGL infos------]");
					Log.message("  Version: " + OpenGLUtils.getOpenGLVersion());
					Log.message("  Vendor: " + OpenGLUtils.getOpenGLVendor());
					Log.message("[------OpenAL infos------]");
					Log.message("  Version: " + AL10.alGetString(AL10.AL_VERSION));
					Log.message("  Vendor: " + AL10.alGetString(AL10.AL_VENDOR));
					// TODO OpenCL infos
				Log.message("[------Engine infos------]");
				Log.message("  Version: " + JGEngine.getEngineVersion());
				Log.message("------------------------");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			renderEngine = new RenderEngine();
			physEngine = new PhysicsEngine();
			RenderEngine.printIfGLError();
			Texture loadingScreenText = null;
			try
			{
				loadingScreenText = renderEngine.loadTexture(classResLoader.getResource(new ResourceLocation("textures", "loadingScreen.png")));
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.exit(-1);
			}
			LoadingScreen loadingScreen = new MonoThreadedLoadingScreen(game);
			loadingScreen.setBackgroundImage(loadingScreenText);
			game.setLoadingScreen(loadingScreen);
			game.drawLoadingScreen("Loading...");

			Properties properties = new Properties();
			File propsFile = new File(game.getGameFolder(), "properties.txt");
			if(!propsFile.exists())
			{
				propsFile.createNewFile();
			}
			properties.load(new FileInputStream(propsFile));
			game.setProperties(properties);
			game.onPropertiesLoad(properties);
			renderEngine.init();
			soundEngine.init();
			physEngine.init();
			game.init();
			while(running && !window.shouldBeClosed() && game.isRunning())
			{
				tick();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		cleanup();
		System.exit(0);
	}   ).start();
	}

	private void cleanup()
	{
		running = false;
		AL.destroy();
		GPUProgramResource.destroyAll();
		CL.destroy();
		JGEngine.disposeAll();
		window.disposeWindow();
		try
		{
			game.saveProperties();
			Log.finish();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private final void tick()
	{
		if(game.getCursor() != null) window.hideCursor();
		double now = System.nanoTime();
		int updateCount = 0;

		if(!paused)
		{
			Input.pollEvents();
			double delta = timeBetweenUpdates / 1000000000;
			while(now - lastUpdateTime > timeBetweenUpdates && updateCount < maxUpdatesBeforeRender)
			{

				update(delta);
				lastUpdateTime += timeBetweenUpdates;
				updateCount++ ;
			}

			if(now - lastUpdateTime > timeBetweenUpdates)
			{
				lastUpdateTime = now - timeBetweenUpdates;
			}

			render(delta);
			windowUpdateTimer.startInvocation();
			window.refresh();
			windowUpdateTimer.endInvocation();
			lastRenderTime = now;
			// Update the frames we got.
			int thisSecond = (int)(lastUpdateTime / 1000000000);
			frame++ ;
			if(thisSecond > lastSecondTime)
			{
				fps = frame;
				double totalTime = (1000.0 * (double)(thisSecond - lastSecondTime)) / (double)frame;
				double totalRecordedTime = 0;
				if(Profiler.display)
				{
					totalRecordedTime += physEngine.displayProfileInfo((double)frame);
					totalRecordedTime += game.displayUpdateTime((double)frame);
					totalRecordedTime += renderEngine.displayRenderTime((double)frame);
					totalRecordedTime += renderEngine.displayWindowSyncTime((double)frame);
					totalRecordedTime += windowUpdateTimer.displayAndReset("Window update time", (double)frame);
					totalRecordedTime += sleepTimer.displayAndReset("Sleep time", (double)frame);
					Log.message("Non-profiled time: " + (totalTime - totalRecordedTime) + " ms");
					Log.message("Total time: " + totalTime + " ms");
					Log.message(fps + " fps");
				}
				frame = 0;
				lastSecondTime = thisSecond;
			}

			while(now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < timeBetweenUpdates)
			{
				sleepTimer.startInvocation();
				Thread.yield();

				try
				{
					Thread.sleep(1);
				}
				catch(Exception e)
				{
				}

				now = System.nanoTime();
				sleepTimer.endInvocation();
			}
		}

		if(!window.isActive()) try
		{
			window.setFullscreen(false);
		}
		catch(EngineException e)
		{
			e.printStackTrace();
		}
	}

	private boolean screenshotKey = false;
	private int	 tick;

	private void update(double delta)
	{
		if(Input.isKeyDown(Input.KEY_F2) && !screenshotKey)
		{
			screenshotKey = true;
			try
			{
				File screenshotsFolder = new File(game.getGameFolder(), "screenshots");
				if(!screenshotsFolder.exists())
				{
					screenshotsFolder.mkdirs();
				}
				ImageIO.write(LWJGLHandler.takeScreenshot(), "png", new File(screenshotsFolder, Strings.createCorrectedFileName(Time.getTimeAsString()) + ".png"));
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		else if(!Input.isKeyDown(Input.KEY_F2) && screenshotKey)
		{
			screenshotKey = false;
		}

		Time.setDelta(delta);
		int lastRecordedTick = tick;
		while(game.getLoadingScreen() != null && !game.getLoadingScreen().isFinished())
		{
			for(Music m : Music.musicsPlaying())
			{
				m.stop();
			}

			if(game.getLoadingScreen().getType() == LoadingScreenType.MULTI_THREADED)
			{
				Display.sync(60);
			}
			tick++ ; // We fake the tick counter to update animations
			tickTickables(true);
			game.getLoadingScreen().refreshScreen();
			game.getLoadingScreen().runFirstTaskGroupAvailable();
		}
		tick = lastRecordedTick;
		physEngine.update(delta);
		soundEngine.update(delta);
		game.update(delta);
		tickTickables(false);
		tick++ ;
	}

	private void tickTickables(boolean inLoadingScreen)
	{
		for(ITickable tickable : tickables)
			tickable.tick(inLoadingScreen);
	}

	public RenderEngine getRenderEngine()
	{
		return renderEngine;
	}

	private void render(double delta)
	{
		window.updateSizeIfNeeded();
		renderEngine.clearBuffers();
		glColor4f(1, 1, 1, 1);
		game.render(renderEngine, delta);
	}

	public ResourceLoader getClasspathResourceLoader()
	{
		return classResLoader;
	}

	public Window getWindow()
	{
		return window;
	}

	public static CoreEngine getCurrent()
	{
		return current;
	}

	public SoundEngine getSoundEngine()
	{
		return soundEngine;
	}

	public PhysicsEngine getPhysicsEngine()
	{
		return physEngine;
	}

	public ResourceLoader getDiskResourceLoader()
	{
		return diskResLoader;
	}

	public int getTick()
	{
		return tick;
	}

	public void kill()
	{
		running = false;
		cleanup();
	}

	public void addTickableObject(ITickable tickable)
	{
		tickables.add(tickable);
	}

}
