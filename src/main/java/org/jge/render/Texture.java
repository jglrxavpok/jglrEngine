package org.jge.render;

import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;
import java.util.HashMap;

import org.jge.AbstractResource;
import org.jge.CoreEngine;
import org.jge.Disposable;
import org.jge.EngineException;
import org.jge.JGEngine;
import org.jge.gpuresources.TextureResource;
import org.jge.util.OpenGLUtils;

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
public class Texture implements Disposable
{

	public static final float								 FILTER_NEAREST  = GL_NEAREST;
	public static final float								 FILTER_LINEAR   = GL_LINEAR;
	private static HashMap<AbstractResource, TextureResource> loadedResources = new HashMap<AbstractResource, TextureResource>();
	private TextureResource								   data;

	private AbstractResource								  res;

	public Texture(int width, int height)
	{
		data = new TextureResource(width, height);
	}

	public Texture(int width, int height, ByteBuffer data, int target, float filter, int attachment, int internalFormat, int format, boolean clamp)
	{
		this.data = new TextureResource(target, 1, width, height, data, filter, attachment, internalFormat, format, clamp);
	}

	public Texture(TextureResource res) throws EngineException
	{
		JGEngine.getDisposer().add(this);
		data = res;
		data.increaseCounter();
	}

	public Texture(AbstractResource res) throws EngineException
	{
		this(res, GL_LINEAR);
	}

	public Texture(AbstractResource res, float filter) throws EngineException
	{
		this.res = res;
		TextureResource existingResource = loadedResources.get(res);

		if(existingResource != null)
		{
			JGEngine.getDisposer().add(this);
			data = existingResource;
			data.increaseCounter();
		}
		else
		{
			data = OpenGLUtils.loadTexture(res, filter);
			loadedResources.put(res, data);
		}
	}

	public int getTarget()
	{
		return data.getTarget();
	}

	public void bind()
	{
		bind(0);
	}

	public void bind(int samplerSlot)
	{
		CoreEngine.getCurrent().getRenderEngine().bindTexture(this, samplerSlot);
	}

	public void dispose()
	{
		if(data.decreaseCounter())
		{
			data.dispose();
			if(res != null) loadedResources.remove(res);
		}
	}

	public int getID()
	{
		return data.getID();
	}

	public Texture bindAsRenderTarget()
	{
		data.bindAsRenderTarget();
		return this;
	}

	public TextureResource getResource()
	{
		return data;
	}

	public int getWidth()
	{
		return data.getWidth();
	}

	public int getHeight()
	{
		return data.getHeight();
	}

	public boolean equals(Object other)
	{
		if(other instanceof Texture) return ((Texture)other).data.getID() == data.getID();
		return false;
	}
}
