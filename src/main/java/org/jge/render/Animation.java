package org.jge.render;

import org.jge.CoreEngine;
import org.jge.ITickable;
import org.jge.Time;
import org.jge.components.Camera;
import org.jge.maths.Transform;
import org.jge.render.shaders.Shader;

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
public class Animation implements ITickable
{

	private double   delayBetweenFrames;
	private boolean  loop;
	private Sprite[] sprites;
	private double   index;

	public static Animation fromAtlas(TextureAtlas atlas, double delayBetweenFramesInSeconds, boolean loop)
	{
		Sprite[] spritesList = new Sprite[atlas.getXNbr() * atlas.getYNbr()];
		int index = 0;
		for(int y = 0; y < atlas.getYNbr(); y++ )
		{
			for(int x = 0; x < atlas.getXNbr(); x++ )
			{
				spritesList[index] = new Sprite(atlas.getTiles()[x][y]).setWidth(64).setHeight(64);
				index++ ;
			}
		}
		return new Animation(delayBetweenFramesInSeconds, loop, spritesList);
	}

	public Animation(double delayBetweenFramesInSeconds, boolean loop, Sprite... sprites)
	{
		CoreEngine.getCurrent().addTickableObject(this);
		this.delayBetweenFrames = delayBetweenFramesInSeconds;
		this.loop = loop;
		this.sprites = sprites;
	}

	public void render(Shader shader, Transform transform, Camera camera, double delta, RenderEngine engine, int tick)
	{
		getSprite().render(shader, transform, camera, delta, engine);
	}

	public Sprite getSprite()
	{
		return sprites[(int)index];
	}

	public double getDelayBetweenFrames()
	{
		return delayBetweenFrames;
	}

	public Animation setDelayBetweenFrames(double delayBetweenFramesInSeconds)
	{
		this.delayBetweenFrames = delayBetweenFramesInSeconds;
		return this;
	}

	public boolean isLooping()
	{
		return loop;
	}

	public Animation setLooping(boolean looping)
	{
		this.loop = looping;
		return this;
	}

	public double getSpriteWidth()
	{
		return getSprite().getWidth();
	}

	public double getSpriteHeight()
	{
		return getSprite().getHeight();
	}

	public void tick(boolean inLoadingScreen)
	{
		index += (1.0 / delayBetweenFrames) * Time.getDelta();
		if((int)index >= sprites.length && loop) index = 0.0;
	}
}
