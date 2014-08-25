package org.jge.components.hud;

import org.jge.components.Camera;
import org.jge.components.SceneObject;
import org.jge.render.RenderEngine;
import org.jge.render.Sprite;
import org.jge.render.shaders.Shader;

public class HUDSpriteObject extends SceneObject
{

	private Sprite sprite;

	public HUDSpriteObject(Sprite testSprite)
	{
		this.sprite = testSprite;
	}
	
	public void render(Shader shader, Camera cam, double delta, RenderEngine engine)
	{
		sprite.render(shader, getTransform(), cam, delta, engine);
	}
}
