package org.jge.util;

import org.jge.AbstractResource;
import org.jge.EngineException;
import org.jge.render.Texture;

public class Cursor
{

	private String  name;
	private Texture text;

	public Cursor(String name, AbstractResource image)
	{
		this.name = name;
		try
		{
			this.text = new Texture(image, Texture.FILTER_NEAREST);
		}
		catch(EngineException e)
		{
			e.printStackTrace();
		}
	}

	public String getName()
	{
		return name;
	}

	public Texture getTexture()
	{
		return text;
	}

}
