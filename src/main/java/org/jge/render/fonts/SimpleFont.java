package org.jge.render.fonts;

import static org.lwjgl.opengl.GL11.*;

import org.jge.CoreEngine;
import org.jge.EngineException;
import org.jge.ResourceLocation;
import org.jge.render.Texture;
import org.jge.render.TextureAtlas;

public class SimpleFont extends Font
{

	static
	{
		try
		{
			instance = new SimpleFont();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static Font instance;

	private SimpleFont() throws EngineException, Exception
	{
		super(new TextureAtlas(new Texture(CoreEngine.getCurrent().getClasspathResourceLoader().getResource(new ResourceLocation("test/textures/font.png")), GL_NEAREST), 16, 16), null);
	}

	public double getCharSpacing(char c, char next)
	{
		return -8;
	}

	@Override
	public float getCharWidth(char c)
	{
		return 16;
	}

	@Override
	public float getCharHeight(char c)
	{
		return 16;
	}

}
