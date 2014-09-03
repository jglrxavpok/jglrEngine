package org.jge.render;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.jge.AbstractResource;
import org.jge.CoreEngine;
import org.jge.EngineException;
import org.jge.ResourceLoader;
import org.jge.ResourceLocation;
import org.jge.util.ImageUtils;
import org.jge.util.Log;
import org.jge.util.OpenGLUtils;
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
 */
public class TextureMap implements IconGenerator
{

	private ResourceLoader						 loader;
	private ResourceLocation					   base;
	private ArrayList<TextureIcon>				 registredIcons;
	private HashMap<ResourceLocation, TextureIcon> registredMap;
	private Texture								texture;

	public TextureMap(ResourceLoader loader, ResourceLocation base)
	{
		this.loader = loader;
		this.base = base;
		registredIcons = new ArrayList<>();
		registredMap = new HashMap<>();
	}

	public ResourceLocation completeLocation(ResourceLocation loc)
	{
		ResourceLocation newLoc = new ResourceLocation(base.getFullPath(), loc.getFullPath());
		return newLoc;
	}

	@Override
	public TextureIcon generateIcon(ResourceLocation loc)
	{
		if(registredMap.containsKey(loc)) return registredMap.get(loc);
		TextureMapIcon icon = new TextureMapIcon(0, 0, 0, 0, 0, 0);
		registredIcons.add(icon);
		registredMap.put(loc, icon);
		return icon;
	}

	public void compile() throws EngineException
	{
		Iterator<Entry<ResourceLocation, TextureIcon>> it = registredMap.entrySet().iterator();
		Stitcher stitcher = new Stitcher();
		HashMap<Integer, TextureIcon> indexes = new HashMap<>();
		while(it.hasNext())
		{
			Entry<ResourceLocation, TextureIcon> entry = it.next();
			ResourceLocation loc = completeLocation(entry.getKey());
			TextureIcon icon = entry.getValue();
			try
			{
				AbstractResource res = loader.getResource(loc);
				BufferedImage img = ImageUtils.loadImage(res);
				indexes.put(stitcher.addImage(img), icon);
			}
			catch(Exception e)
			{
				Log.error("Unable to find icon: " + loc.getFullPath());
			}

		}

		BufferedImage stitchedImage = stitcher.stitch();
		Iterator<Integer> indexesIt = indexes.keySet().iterator();
		while(indexesIt.hasNext())
		{
			int index = indexesIt.next();
			TextureIcon icon = indexes.get(index);
			((TextureMapIcon)icon).setMinU(stitcher.getMinU(index));
			((TextureMapIcon)icon).setMinV(stitcher.getMinV(index));
			((TextureMapIcon)icon).setMaxU(stitcher.getMaxU(index));
			((TextureMapIcon)icon).setMaxV(stitcher.getMaxV(index));
			((TextureMapIcon)icon).setWidth(stitcher.getWidth(index));
			((TextureMapIcon)icon).setHeight(stitcher.getHeight(index));
		}

		if(CoreEngine.getCurrent().isDebug())
		{
			if(stitchedImage != null)
			{
				try
				{
					ImageIO.write(stitchedImage, "png", new File(".", Strings.createCorrectedFileName(this.base.getFullPath()) + ".png"));
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		texture = new Texture(OpenGLUtils.loadTexture(stitchedImage, Texture.FILTER_NEAREST));
	}

	private class TextureMapIcon implements TextureIcon
	{
		private float minu;
		private float maxu;
		private float minv;
		private float maxv;
		private int   width;
		private int   height;

		TextureMapIcon(float minu, float minv, float maxu, float maxv, int width, int height)
		{
			this.minu = minu;
			this.maxu = maxu;
			this.minv = minv;
			this.maxv = maxv;
			this.width = width;
			this.height = height;
		}

		@Override
		public double getWidth()
		{
			return width;
		}

		@Override
		public double getHeight()
		{
			return height;
		}

		@Override
		public double getMinU()
		{
			return minu;
		}

		@Override
		public double getMaxU()
		{
			return maxu;
		}

		@Override
		public double getMinV()
		{
			return minv;
		}

		@Override
		public double getMaxV()
		{
			return maxv;
		}

		public void setMinU(float minu)
		{
			this.minu = minu;
		}

		public void setMaxU(float maxu)
		{
			this.maxu = maxu;
		}

		public void setMinV(float minv)
		{
			this.minv = minv;
		}

		public void setMaxV(float maxv)
		{
			this.maxv = maxv;
		}

		public void setWidth(int width)
		{
			this.width = width;
		}

		public void setHeight(int height)
		{
			this.height = height;
		}
	}

	public Texture getTexture()
	{
		return texture;
	}
}
