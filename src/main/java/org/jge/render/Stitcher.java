package org.jge.render;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.jge.maths.Maths;
import org.jge.util.ImageUtils;
import org.jge.util.Log;

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
public class Stitcher
{
	private ArrayList<BufferedImage> imgs;
	private ArrayList<Slot>		  slots;
	private int					  tileWidth;
	private int					  tileHeight;
	private BufferedImage			emptySlotImage;

	public Stitcher(BufferedImage emptyImage)
	{
		this.emptySlotImage = emptyImage;
		slots = new ArrayList<>();
		imgs = new ArrayList<>();
		tileWidth = -1;
		tileHeight = -1;
	}

	public int addImage(BufferedImage img)
	{
		return addImage(img, false);
	}

	public int addImage(BufferedImage img, boolean forceResize)
	{
		if(tileWidth == -1 || tileHeight == -1)
		{
			tileWidth = img.getWidth();
			tileHeight = img.getHeight();
		}
		else if(!forceResize && (img.getWidth() != tileWidth || img.getHeight() != tileHeight))
		{
			Log.fatal("Unexpected size: " + img.getWidth() + "x" + img.getHeight() + "px, expected " + tileWidth + "x" + tileHeight + "px. Image index: " + imgs.size());
		}
		imgs.add(ImageUtils.resize(img, tileWidth, tileHeight));
		return imgs.size() - 1;
	}

	public BufferedImage stitch()
	{
		int nbrY = Maths.upperPowerOf2((int)Maths.floor(Maths.sqrt(imgs.size())));
		int nbrX = (int)Maths.roundToNearest((double)imgs.size() / (double)nbrY);
		int width = nbrX * tileWidth;
		int height = nbrY * tileHeight;
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = result.createGraphics();
		for(int i = 0; i < imgs.size(); i++ )
		{
			int column = i % nbrX;
			int row = i / nbrX;
			int x = column * tileWidth;
			int y = row * tileHeight;
			g.drawImage(imgs.get(i), column * tileWidth, row * tileHeight, null);
			slots.add(new Slot((float)x / (float)width, (float)y / (float)height, (float)(x + tileWidth) / (float)width, (float)(y + tileHeight) / (float)height, width, height));
		}

		emptySlotImage = ImageUtils.resize(emptySlotImage, tileWidth, tileHeight);
		for(int n = imgs.size(); n < nbrX * nbrY; n++ )
		{
			int column = n % nbrX;
			int row = n / nbrX;
			g.drawImage(emptySlotImage, column * tileWidth, row * tileHeight, null);
		}
		g.dispose();
		return result;
	}

	public float getMinU(int index)
	{
		return slots.get(index).minU;
	}

	public float getMinV(int index)
	{
		return slots.get(index).minV;
	}

	public float getMaxU(int index)
	{
		return slots.get(index).maxU;
	}

	public float getMaxV(int index)
	{
		return slots.get(index).maxV;
	}

	public int getWidth(int index)
	{
		return slots.get(index).width;
	}

	public int getHeight(int index)
	{
		return slots.get(index).height;
	}

	private class Slot
	{
		public float minU;
		public float minV;
		public float maxU;
		public float maxV;
		public int   width;
		public int   height;

		Slot(float minU, float minV, float maxU, float maxV, int width, int height)
		{
			this.minU = minU;
			this.minV = minV;
			this.maxU = maxU;
			this.maxV = maxV;
			this.width = width;
			this.height = height;
		}
	}
}
