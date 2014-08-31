package org.jge;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.swing.JFrame;

import org.jge.maths.Maths;
import org.jge.util.ImageUtils;
import org.jge.util.Log;
import org.jge.util.OpenGLUtils;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

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
public class Window
{

	private double				 fpsCap;
	private int					w;
	private int					h;
	private boolean				init;
	private boolean				shouldGoFullscreen;
	private boolean				isFullscreen;
	private JFrame				 parentFrame;
	private Canvas				 parentCanvas;
	private String				 title;
	private boolean				stop;
	protected boolean			  preventStop;
	private boolean				vsync;
	private org.lwjgl.input.Cursor emptyCursor;
	private static Window		  current;

	public Window(int w, int h) throws EngineException
	{
		try
		{
			setTitle("Game running JGEngine v" + JGEngine.getEngineVersion());
			fpsCap = 60;
			this.w = w;
			this.h = h;
			current = this;
		}
		catch(Exception e)
		{
			throw new EngineException("Error during window initialization:", e);
		}
	}

	public void init() throws EngineException
	{
		try
		{
			init = true;
			parentFrame = new JFrame();
			parentFrame.addWindowListener(new WindowAdapter()
			{
				@Override
				public void windowClosing(WindowEvent e)
				{
					if(!preventStop) stop = true;
				}
			});
			parentCanvas = new Canvas();
			setTitle(title);
			parentCanvas.setPreferredSize(new Dimension(w, h));
			parentFrame.add(parentCanvas);
			parentFrame.pack();
			if(!shouldGoFullscreen)
				parentFrame.setLocationRelativeTo(null);
			else
				parentFrame.setLocation(0, 0);
			parentFrame.setAlwaysOnTop(false);
			setDisplayMode(w, h, shouldGoFullscreen);
			Display.setParent(parentCanvas);
			ContextAttribs context = new ContextAttribs(3, 3);
			if(System.getProperty("os.name").contains("Mac")) context = new ContextAttribs(3, 2);
			Display.create(new PixelFormat().withDepthBits(16), context.withDebug(true).withProfileCore(true).withProfileCompatibility(true));
			Display.setVSyncEnabled(vsync);
			parentFrame.setTitle(title);
			parentFrame.setVisible(true);
		}
		catch(Exception e)
		{
			throw new EngineException("Error during window initialization:", e);
		}
	}

	public static Window getCurrent()
	{
		return current;
	}

	public Window setSize(int w, int h) throws EngineException
	{
		setDisplayMode(w, h, isFullscreen);
		return this;
	}

	public boolean shouldBeClosed()
	{
		return stop || Display.isCloseRequested();
	}

	public Window setLocation(int x, int y)
	{
		Display.setLocation(x, y);
		return this;
	}

	public int getWidth()
	{
		return Display.getWidth();
	}

	public int getHeight()
	{
		return Display.getHeight();
	}

	public int getLocationX()
	{
		return Display.getX();
	}

	public int getLocationY()
	{
		return Display.getY();
	}

	public Window setIcons(AbstractResource... res) throws EngineException
	{
		try
		{
			ByteBuffer[] icons = new ByteBuffer[res.length];
			BufferedImage[] imgs = new BufferedImage[res.length];

			for(int i = 0; i < icons.length; i++ )
			{
				BufferedImage img = ImageUtils.loadImage(res[i]);
				imgs[i] = img;
				if(img != null)
					icons[i] = OpenGLUtils.imageToByteBuffer(img);
				else
					throw new EngineException("Error while setting icon of window : invalid image");
			}
			parentFrame.setIconImages(Arrays.asList(imgs));
			Display.setIcon(icons);
		}
		catch(EngineException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new EngineException("Error while setting icon of window", e);
		}

		return this;
	}

	public Window setFPSCap(double cap)
	{
		this.fpsCap = cap;
		return this;
	}

	public double getFPSCap()
	{
		return fpsCap;
	}

	public boolean isFullscreen()
	{
		return isFullscreen;
	}

	public Window setFullscreen(boolean fullscreen) throws EngineException
	{
		isFullscreen = fullscreen;
		if(!init)
		{
			shouldGoFullscreen = fullscreen;
			return this;
		}
		setDisplayMode(w, h, fullscreen);
		return this;
	}

	private void setDisplayMode(int width, int height, boolean fullscreen)
	{
		preventStop = true;
		this.w = width;
		this.h = height;
		// return if requested DisplayMode is already set
		if((Display.getDisplayMode().getWidth() == width) && (Display.getDisplayMode().getHeight() == height) && (Display.isFullscreen() == fullscreen))
		{
			preventStop = false;
			return;
		}

		try
		{
			DisplayMode targetDisplayMode = null;

			if(fullscreen)
			{
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				int freq = 0;

				for(int i = 0; i < modes.length; i++ )
				{
					DisplayMode current = modes[i];

					if((current.getWidth() == width) && (current.getHeight() == height))
					{
						if((targetDisplayMode == null) || (current.getFrequency() >= freq))
						{
							if((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel()))
							{
								targetDisplayMode = current;
								freq = targetDisplayMode.getFrequency();
							}
						}

						// if we've found a match for bpp and frequence against
						// the
						// original display mode then it's probably best to go
						// for this one
						// since it's most likely compatible with the monitor
						if((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) && (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency()))
						{
							targetDisplayMode = current;
							break;
						}
					}
				}
			}
			else
			{
				targetDisplayMode = new DisplayMode(width, height);

				parentFrame.dispose();
				parentCanvas.setPreferredSize(new Dimension(width, height));
				parentFrame.setUndecorated(false);
				parentFrame.pack();
				parentFrame.setLocationRelativeTo(null);
				parentFrame.setVisible(true);
				parentFrame.setAlwaysOnTop(false);
			}

			if(targetDisplayMode == null)
			{
				Log.message("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
				if(fullscreen)
				{
					Log.message("Switching to fullscreen window with no borders");
					parentFrame.dispose();
					parentFrame.setUndecorated(true);
					parentCanvas.setPreferredSize(new Dimension(Display.getDesktopDisplayMode().getWidth(), Display.getDesktopDisplayMode().getHeight()));
					parentFrame.pack();
					parentFrame.setLocation(0, 0);
					parentFrame.setVisible(true);
					parentFrame.setAlwaysOnTop(false);
				}

				preventStop = false;
				return;
			}

			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);

		}
		catch(LWJGLException e)
		{
			Log.message("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
		}
		preventStop = false;
	}

	public Window setTitle(String title)
	{
		if(title == null) title = "";
		this.title = title;
		if(init)
		{
			parentFrame.setTitle(title);
			Display.setTitle(title);
		}
		return this;
	}

	public boolean isActive()
	{
		return Display.isActive() || parentFrame.isActive();
	}

	public int getPhysicalWidth()
	{
		return (int)Maths.max(parentCanvas.getWidth(), Display.getWidth());
	}

	public int getPhysicalHeight()
	{
		return (int)Maths.max(parentCanvas.getHeight(), Display.getHeight());
	}

	public void disposeWindow()
	{
		Display.destroy();
		parentFrame.dispose();
	}

	public Window refresh()
	{
		Display.update();
		return this;
	}

	public Window setVsync(boolean b)
	{
		vsync = b;
		if(init)
		{
			Display.setVSyncEnabled(b);
		}
		return this;
	}

	public Window bindAsRenderTarget()
	{
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, getPhysicalWidth(), getPhysicalHeight());
		CoreEngine.getCurrent().getRenderEngine().setRenderTarget(null);
		return this;
	}

	public void hideCursor()
	{
		try
		{
			if(emptyCursor == null) emptyCursor = new org.lwjgl.input.Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(4), BufferUtils.createIntBuffer(1));
			if(Mouse.getNativeCursor() != emptyCursor)
			{
				Mouse.setNativeCursor(emptyCursor);
			}
		}
		catch(LWJGLException e)
		{
			e.printStackTrace();
		}
	}

}
