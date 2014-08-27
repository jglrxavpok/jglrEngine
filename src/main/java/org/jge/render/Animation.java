package org.jge.render;

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
public class Animation
{

	private double   speedInTicks;
	private boolean  loop;
	private Sprite[] sprites;
	private int	  index;

	public Animation(double speedInTicks, boolean loop, Sprite... sprites)
	{
		this.speedInTicks = speedInTicks;
		this.loop = loop;
		this.sprites = sprites;
	}

	public void render(Shader shader, Transform transform, Camera camera, double delta, RenderEngine engine, int tick)
	{
		getSpriteForTick(tick).render(shader, transform, camera, delta, engine);
	}

	public Sprite getSpriteForTick(int tick)
	{
		updateIndex(tick);
		return sprites[index];
	}

	private void updateIndex(int tick)
	{
		index = (int)((tick * speedInTicks) % sprites.length);
	}

	public double getSpeedInTicks()
	{
		return speedInTicks;
	}

	public void setSpeedInTicks(double speedInTicks)
	{
		this.speedInTicks = speedInTicks;
	}

	public boolean isLooping()
	{
		return loop;
	}

	public void setLooping(boolean looping)
	{
		this.loop = looping;
	}
}
