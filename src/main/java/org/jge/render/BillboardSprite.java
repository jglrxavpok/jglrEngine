package org.jge.render;

import org.jge.CoreEngine;
import org.jge.components.Camera;
import org.jge.components.SceneObject;
import org.jge.maths.Matrix4;
import org.jge.maths.Quaternion;
import org.jge.maths.Transform;
import org.jge.maths.Vector3;
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
public class BillboardSprite extends SceneObject
{

	private Animation animation;
	private Transform transform;
	private Sprite	sprite;

	public BillboardSprite(Sprite sprite)
	{
		this.transform = new Transform();
		this.sprite = sprite;
	}

	public BillboardSprite(Animation animation)
	{
		this.transform = new Transform();
		this.animation = animation;
	}

	public BillboardSprite setAnimation(Animation animation)
	{
		this.animation = animation;
		return this;
	}

	public BillboardSprite setSprite(Sprite sprite)
	{
		this.sprite = sprite;
		return this;
	}

	public void render(Shader shader, Camera camera, double delta, RenderEngine engine)
	{
		Vector3 look = camera.getParent().getTransform().getTransformedPos().sub(transform.getTransformedPos()).normalize();
		Vector3 right = camera.getParent().getTransform().getTransformedRotation().getUp().cross(look).normalize();
		Vector3 up = look.cross(right).normalize();
		Matrix4 rotMatrix = new Matrix4().initRotation(look, up, right);
		transform.setScale(getTransform().getTransformedScale());
		transform.setPosition(getTransform().getTransformedPos());
		transform.setRotation(new Quaternion(rotMatrix));
		if(animation != null) animation.render(shader, transform, camera, delta, engine, CoreEngine.getCurrent().getTick());
		if(sprite != null) sprite.render(shader, transform, camera, delta, engine);
	}
}
