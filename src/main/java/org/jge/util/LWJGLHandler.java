package org.jge.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.IntBuffer;

import org.jge.JGEngine;
import org.jge.crash.CrashReport;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

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
 */
public class LWJGLHandler
{

	private static boolean loaded;

	/**
	 * @throws Exception
	 */
	public static void load(File folder) throws Exception
	{
		if(!loaded)
		{
			if(!folder.exists()) folder.mkdirs();
			if(folder.isDirectory())
			{
				installNatives(folder);
				System.setProperty("org.lwjgl.librarypath", folder.getAbsolutePath());
			}
			loaded = true;
		}
	}

	private static void installNatives(File folder) throws Exception
	{
		OperatingSystem os = SystemUtils.getOS();
		Log.message("OS found : " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
		Log.message("Installing natives...");
		if(os == OperatingSystem.WINDOWS)
		{
			if(!new File(folder.getPath() + "/jinput-dx8_64.dll").exists())
			{
				extractFromClasspath("/windows/jinput-dx8_64.dll", folder);
				extractFromClasspath("/windows/jinput-dx8.dll", folder);
				extractFromClasspath("/windows/jinput-raw_64.dll", folder);
				extractFromClasspath("/windows/jinput-raw.dll", folder);
				extractFromClasspath("/windows/lwjgl.dll", folder);
				extractFromClasspath("/windows/lwjgl64.dll", folder);
				extractFromClasspath("/windows/OpenAL32.dll", folder);
				extractFromClasspath("/windows/OpenAL64.dll", folder);
			}
			else
			{
				Log.message("Natives already exist.");
			}
		}
		else if(os == OperatingSystem.SOLARIS)
		{
			if(!new File(folder.getPath() + "/liblwjgl.so").exists())
			{
				extractFromClasspath("/solaris/liblwjgl.so", folder);
				extractFromClasspath("/solaris/liblwjgl64.so", folder);
				extractFromClasspath("/solaris/libopenal.so", folder);
				extractFromClasspath("/solaris/libopenal64.so", folder);
			}
			else
			{
				Log.message("Natives already exist.");
			}

		}
		else if(os == OperatingSystem.LINUX)
		{
			if(!new File(folder.getPath() + "/liblwjgl.so").exists())
			{
				extractFromClasspath("/linux/liblwjgl.so", folder);
				extractFromClasspath("/linux/liblwjgl64.so", folder);
				extractFromClasspath("/linux/libopenal.so", folder);
				extractFromClasspath("/linux/libopenal64.so", folder);
			}
			else
			{
				Log.message("Natives already exist.");
			}

		}
		else if(os == OperatingSystem.MACOSX)
		{
			if(!new File(folder.getPath() + "/openal.dylib").exists())
			{
				extractFromClasspath("/macosx/liblwjgl.jnilib", folder);
				extractFromClasspath("/macosx/liblwjgl-osx.jnilib", folder);
				extractFromClasspath("/macosx/openal.dylib", folder);
			}
			else
			{
				Log.message("Natives already exist.");
			}
		}
		else
		{
			JGEngine.crash(new CrashReport("Unknown OS: " + System.getProperty("os.name")));
		}
		System.setProperty("net.java.games.input.librarypath", folder.getAbsolutePath());
	}

	private static void extractFromClasspath(String fileName, File folder)
	{
		String[] split = fileName.split("/");
		String diskFileName = split[split.length - 1];
		try(FileOutputStream in = new FileOutputStream(new File(folder, diskFileName)))
		{
			IO.copy(LWJGLHandler.class.getResourceAsStream(fileName), in);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private static int[]	 screenshotBufferArray;
	private static IntBuffer screenshotBuffer;

	public static BufferedImage takeScreenshot()
	{
		int k = Display.getWidth() * Display.getHeight();
		if(screenshotBuffer == null || screenshotBuffer.capacity() < k)
		{
			screenshotBuffer = BufferUtils.createIntBuffer(k);
			screenshotBufferArray = new int[k];
		}
		GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		screenshotBuffer.clear();
		GL11.glReadPixels(0, 0, Display.getWidth(), Display.getHeight(), GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, screenshotBuffer);
		screenshotBuffer.get(screenshotBufferArray);
		int[] aint1 = new int[Display.getWidth()];
		int j = Display.getHeight() / 2;
		for(int l = 0; l < j; ++l)
		{
			System.arraycopy(screenshotBufferArray, l * Display.getWidth(), aint1, 0, Display.getWidth());
			System.arraycopy(screenshotBufferArray, (Display.getHeight() - 1 - l) * Display.getWidth(), screenshotBufferArray, l * Display.getWidth(), Display.getWidth());
			System.arraycopy(aint1, 0, screenshotBufferArray, (Display.getHeight() - 1 - l) * Display.getWidth(), Display.getWidth());
		}
		BufferedImage bufferedimage = new BufferedImage(Display.getWidth(), Display.getHeight(), BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < screenshotBufferArray.length; i++ )
		{
			Color c = ImageUtils.getColor(screenshotBufferArray[i]);
			screenshotBufferArray[i] = new Color(c.getBlue(), c.getGreen(), c.getRed()).getRGB();
		}
		bufferedimage.setRGB(0, 0, Display.getWidth(), Display.getHeight(), screenshotBufferArray, 0, Display.getWidth());
		return bufferedimage;
	}
}
