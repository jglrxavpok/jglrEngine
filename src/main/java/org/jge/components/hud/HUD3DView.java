package org.jge.components.hud;

import static org.lwjgl.opengl.GL11.*;

import org.jge.Window;
import org.jge.components.Camera;
import org.jge.components.SceneObject;
import org.jge.gpuresources.TextureResource;
import org.jge.render.RenderEngine;
import org.jge.render.Sprite;
import org.jge.render.Texture;
import org.jge.render.shaders.Shader;
import org.lwjgl.opengl.GL30;

public class HUD3DView extends HUDWidget
{

	private Texture renderTexture;
	private Texture renderTextureTempTarget;
	private Camera cam;
	private Sprite sprite;
	private SceneObject view;
	private int invincation;

	public HUD3DView(double w, double h)
	{
		super(w, h);
	}
	
	public void init()
	{
		super.init();
		renderTexture = new Texture((int)getWidth(), (int)getHeight(),null,GL_TEXTURE_2D,GL_NEAREST,GL30.GL_COLOR_ATTACHMENT0, GL30.GL_RGBA32F, GL_RGBA, false);
		renderTextureTempTarget = new Texture((int)getWidth(), (int)getHeight(),null,GL_TEXTURE_2D,GL_NEAREST,GL30.GL_COLOR_ATTACHMENT0, GL30.GL_RGBA32F, GL_RGBA, false);
		renderMaterial.setTexture("diffuse", renderTexture);
		sprite = new Sprite(renderTexture);
	}
	
	public HUD3DView setViewRoot(SceneObject root)
	{
		this.view = root;
		return this;
	}
	
	public HUD3DView setViewCamera(Camera camera)
	{
		this.cam = camera;
		return this;
	}
	
	public void onPostRender(double delta, RenderEngine renderEngine)
	{
		invincation = 0;
	}
	
	public void render(Shader shader, Camera cam, double delta, RenderEngine engine)
	{
		if(invincation > 1)
			return;
		invincation++;
		if(this.cam == null || view == null)
		{
			TextureResource oldid = engine.getRenderTarget();
			renderTexture.bindAsRenderTarget();
			glClearColor(0, 0, 0, 1);
			if(oldid == null)
				Window.getCurrent().bindAsRenderTarget();
			else
				oldid.bindAsRenderTarget();
		}
		else
		{
			Camera oldCam = engine.getCamera();
			engine.setCamera(this.cam);
			engine.pushState();
//			engine.renderScene(view, renderTexture, renderTextureTempTarget, delta);
			engine.popState();
			engine.setCamera(oldCam);
		}
		
		shader.bind();
		shader.updateUniforms(getTransform(), cam, renderMaterial, engine);
		sprite.render(shader, getTransform(), cam, delta, engine);
	}

}