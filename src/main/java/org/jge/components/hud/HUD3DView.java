package org.jge.components.hud;

import static org.lwjgl.opengl.GL11.*;

import org.jge.Window;
import org.jge.components.Camera;
import org.jge.components.SceneObject;
import org.jge.gpuresources.TextureResource;
import org.jge.render.RenderEngine;
import org.jge.render.RenderState;
import org.jge.render.Sprite;
import org.jge.render.Texture;
import org.jge.render.shaders.Shader;

import org.lwjgl.opengl.GL30;

public class HUD3DView extends HUDWidget
{

	private Texture	 renderTexture;
	private Texture	 renderTextureTempTarget;
	private Camera	  viewCamera;
	private Sprite	  sprite;
	private SceneObject viewRoot;

	public HUD3DView(float w, float h)
	{
		super(w, h);
	}

	public void init()
	{
		super.init();
		renderTexture = new Texture((int)getWidth(), (int)getHeight(), null, GL_TEXTURE_2D, GL_NEAREST, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_RGBA32F, GL_RGBA, false);
		renderTextureTempTarget = new Texture((int)getWidth(), (int)getHeight(), null, GL_TEXTURE_2D, GL_NEAREST, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_RGBA32F, GL_RGBA, false);
		sprite = new Sprite(renderTexture);
		sprite.flip(false, true);
	}

	public HUD3DView setViewRoot(SceneObject root)
	{
		this.viewRoot = root;
		return this;
	}

	public HUD3DView setViewCamera(Camera camera)
	{
		this.viewCamera = camera;
		return this;
	}

	public void onPostRender(double delta, RenderEngine renderEngine)
	{
	}

	public void render(Shader shader, Camera cam, double delta, RenderEngine engine)
	{
		TextureResource oldid = engine.getRenderTarget();
		renderTexture.bindAsRenderTarget();
		glClearColor(0, 0, 0, 1);
		glClear(GL_COLOR_BUFFER_BIT);
		if(this.viewCamera != null && viewRoot != null)
		{
			RenderState prevState = engine.getRenderState();
			engine.popState();
			engine.renderScene(viewRoot, viewCamera, renderTexture, renderTextureTempTarget, delta);
			engine.pushState();
			prevState.apply(engine);
		}
		if(oldid == null)
			Window.getCurrent().bindAsRenderTarget();
		else
			oldid.bindAsRenderTarget();

		sprite.render(shader, getTransform(), cam, delta, engine);
	}

}
