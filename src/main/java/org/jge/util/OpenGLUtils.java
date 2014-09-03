package org.jge.util;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.HashMap;

import org.jge.AbstractResource;
import org.jge.EngineException;
import org.jge.gpuresources.TextureResource;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class OpenGLUtils
{

	private static HashMap<AbstractResource, TextureResource> texturesIds = new HashMap<AbstractResource, TextureResource>();
	private static HashMap<Integer, String>				   capNamesMap = new HashMap<Integer, String>();

	public static void loadCapNames()
	{
		loadCapNames(GL11.class);
		loadCapNames(GL20.class);
		loadCapNames(GL30.class);
		loadCapNames(GL32.class);
	}

	private static void loadCapNames(Class<?> glClass)
	{
		Field[] fields = glClass.getFields();
		for(Field field : fields)
		{
			if(Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()))
			{
				try
				{
					if(field.getType() == Integer.TYPE)
					{
						int value = (Integer)field.get(null);
						if(!capNamesMap.containsKey(value))
						{
							capNamesMap.put(value, field.getName());
						}
					}
				}
				catch(IllegalArgumentException e)
				{
					e.printStackTrace();
				}
				catch(IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public static String getOpenGLVersion()
	{
		return glGetString(GL_VERSION);
	}

	public static String getOpenGLVendor()
	{
		return glGetString(GL_VENDOR);
	}

	public static TextureResource loadTexture(AbstractResource res) throws EngineException
	{
		return loadTexture(res, GL_LINEAR);
	}

	public static TextureResource loadTexture(AbstractResource res, float filter) throws EngineException
	{
		if(texturesIds.containsKey(res)) return texturesIds.get(res);

		BufferedImage img = ImageUtils.loadImage(res);
		if(img == null) throw new EngineException("Couldn't load texture " + res.getResourceLocation().getFullPath() + " : couldn't read");

		return loadTexture(img, filter);
	}

	public static ByteBuffer imageToByteBuffer(BufferedImage img)
	{
		int[] pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());

		int bufLen = pixels.length * 4;
		ByteBuffer oglPixelBuf = BufferUtils.createByteBuffer(bufLen);

		for(int y = 0; y < img.getHeight(); y++ )
		{
			for(int x = 0; x < img.getWidth(); x++ )
			{
				int rgb = pixels[y * img.getWidth() + x];
				float a = ((rgb >> 24) & 0xFF) / 255f;
				float r = ((rgb >> 16) & 0xFF) / 255f;
				float g = ((rgb >> 8) & 0xFF) / 255f;
				float b = ((rgb >> 0) & 0xFF) / 255f;
				oglPixelBuf.put((byte)(r * 255f));
				oglPixelBuf.put((byte)(g * 255f));
				oglPixelBuf.put((byte)(b * 255f));
				oglPixelBuf.put((byte)(a * 255f));
			}
		}

		oglPixelBuf.flip();
		return oglPixelBuf;
	}

	public static String getCapName(int cap)
	{
		if(!capNamesMap.containsKey(cap)) return "" + cap;
		return capNamesMap.get(cap);
	}

	public static TextureResource loadTexture(BufferedImage img, float filter)
	{
		ByteBuffer oglPixelBuf = imageToByteBuffer(img);
		TextureResource resource = new TextureResource(GL_TEXTURE_2D, 1, img.getWidth(), img.getHeight(), oglPixelBuf, filter, GL30.GL_RGBA32F, GL_RGBA, false);

		GL11.glBindTexture(resource.getTarget(), 0);

		return resource;
	}
}
