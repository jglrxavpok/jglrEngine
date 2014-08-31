package org.jge.game;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import org.jge.CoreEngine;
import org.jge.EngineException;
import org.jge.JGEngine;
import org.jge.LoadingScreen;
import org.jge.Profiler.ProfileTimer;
import org.jge.ResourceLoader;
import org.jge.Window;
import org.jge.components.SceneObject;
import org.jge.crash.CrashReport;
import org.jge.phys.PhysicsEngine;
import org.jge.render.RenderEngine;
import org.jge.sound.SoundEngine;
import org.jge.util.Cursor;
import org.jge.util.Strings;

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
public abstract class Game
{

	private File		  folder;
	private boolean	   running	 = true;
	private CoreEngine	engine;
	private SceneRoot	 root;
	private ProfileTimer  updateTimer = new ProfileTimer();
	private Cursor		cursor;
	private LoadingScreen loadingScreen;
	private Properties	properties;

	public void crash(CrashReport report)
	{
		JGEngine.crash(report);
	}

	public LoadingScreen getLoadingScreen()
	{
		return loadingScreen;
	}

	public Game setLoadingScreen(LoadingScreen screen)
	{
		this.loadingScreen = screen;
		if(screen != null)
		{
			Input.lockCursor(false);
		}
		return this;
	}

	public CoreEngine getEngine()
	{
		return engine;
	}

	public Game setEngine(CoreEngine core)
	{
		this.engine = core;
		return this;
	}

	public Game setCursor(Cursor cursor)
	{
		this.cursor = cursor;
		return this;
	}

	public Cursor getCursor()
	{
		return cursor;
	}

	public void updateGame(double delta)
	{
	}

	public final void update(double delta)
	{
		updateTimer.startInvocation();
		getSceneRoot().updateAll(delta);
		updateGame(delta);
		updateTimer.endInvocation();
	}

	public double displayWindowSyncTime()
	{
		return displayUpdateTime(0);
	}

	public double displayUpdateTime(double divisor)
	{
		return updateTimer.displayAndReset("Game update", divisor);
	}

	public ResourceLoader getClasspathResourceLoader()
	{
		return engine.getClasspathResourceLoader();
	}

	public Window getWindow()
	{
		return engine.getWindow();
	}

	public File getGameFolder()
	{
		if(folder == null)
		{
			folder = new File(JGEngine.getGamesFolder(), Strings.createCorrectedFileName(getGameName()));
			if(!folder.exists()) folder.mkdirs();
		}
		return folder;
	}

	public abstract String getGameName();

	public void drawLoadingScreen(String message)
	{
		engine.getRenderEngine().clearBuffers();
		if(loadingScreen != null) this.loadingScreen.refreshScreen();
	}

	public void init()
	{

	}

	public RenderEngine getRenderEngine()
	{
		return engine.getRenderEngine();
	}

	public PhysicsEngine getPhysicsEngine()
	{
		return engine.getPhysicsEngine();
	}

	public SoundEngine getSoundEngine()
	{
		return engine.getSoundEngine();
	}

	public Game addToHUD(SceneObject object)
	{
		getSceneRoot().getChild("hud").addChild(object);
		object.onAddToSceneAll(getSceneRoot().getChild("hud"));
		return this;
	}

	public Game addToScene(SceneObject object)
	{
		getSceneRoot().addChild(object);
		object.onAddToSceneAll(getSceneRoot());
		return this;
	}

	public Game addToWorld(SceneObject object)
	{
		return addToWorld(object.getClass().getCanonicalName() + "_" + object.hashCode(), object);
	}

	public Game addToWorld(String name, SceneObject object)
	{
		getSceneRoot().getChild("world").addChildAs(name, object);
		object.onAddToSceneAll(getSceneRoot());
		return this;
	}

	protected SceneObject getSceneRoot()
	{
		if(root == null)
		{
			root = new SceneRoot();
		}
		return root;
	}

	public final void render(RenderEngine renderEngine, double delta)
	{
		renderEngine.setHUDObject((HUDObject)root.getChild("hud"));
		renderEngine.render(root.getChild("world"), delta);
	}

	public boolean isRunning()
	{
		return running;
	}

	public void stop()
	{
		this.running = false;
	}

	public void onPropertiesLoad(Properties loaded)
	{

	}

	public Properties getProperties()
	{
		return properties;
	}

	public Game saveProperties() throws EngineException
	{
		File propsFile = new File(getGameFolder(), "properties.txt");
		try
		{
			properties.store(new FileOutputStream(propsFile), "");
		}
		catch(Exception e)
		{
			throw new EngineException("Error while saving properties", e);
		}
		return this;
	}

	public ResourceLoader getDiskResourceLoader()
	{
		return engine.getDiskResourceLoader();
	}

	public void setProperties(Properties properties)
	{
		this.properties = properties;
	}
}
