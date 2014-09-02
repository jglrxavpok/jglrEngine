package org.jge.game;

import static org.lwjgl.opengl.GL11.*;

import org.jge.CoreEngine;
import org.jge.Window;
import org.jge.components.Camera;
import org.jge.components.SceneObject;
import org.jge.maths.Matrix4;
import org.jge.maths.Transform;
import org.jge.maths.Vector3;
import org.jge.render.RenderEngine;
import org.jge.render.Sprite;
import org.jge.render.shaders.Shader;

public class HUDObject extends SceneObject
{

	private Camera hudCam;
	private Sprite cursorSprite;

	public HUDObject()
	{
		this.hudCam = new Camera(new Matrix4().initOrthographic(0, Window.getCurrent().getPhysicalWidth(), 0, Window.getCurrent().getPhysicalHeight(), -1, 1));
		hudCam.setName("hud");
		addComponentAs("hudCam", hudCam);
	}

	public void render(Shader shader, Camera cam, double delta, RenderEngine renderEngine)
	{
		glClear(GL_DEPTH_BUFFER_BIT);
	}

	public void renderAll(Shader shader, Camera cam, double delta, RenderEngine renderEngine)
	{
		if(renderEngine.isRenderingShadowMap()) return;
		renderEngine.pushState();
		renderEngine.enableGLCap(GL_BLEND);
		renderEngine.disableGLCap(GL_DEPTH_TEST);
		renderEngine.enableGLCap(GL_ALPHA_TEST);
		renderEngine.setAlphaFunc(GL_GEQUAL, 0.001f);
		renderEngine.setBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		renderEngine.setLightMatrix(new Matrix4().initIdentity());
		renderEngine.disableGLCap(GL_CULL_FACE);
		renderEngine.setLighting(false);
		renderEngine.setShadowing(false);
		renderEngine.setParallaxDispMapping(false);
		renderEngine.setBoolean("normalMapping", false);

		renderEngine.setAmbientColor(1, 1, 1);
		super.renderAll(shader, hudCam, delta, renderEngine);
		if(CoreEngine.getCurrent().getGame().getCursor() != null && !Input.isMouseCursorLocked())
		{
			Transform transform = new Transform();
			transform.setPosition(Vector3.get(Input.getMouseX(), Input.getMouseY() - CoreEngine.getCurrent().getGame().getCursor().getTexture().getHeight(), 0));
			if(cursorSprite == null)
			{
				cursorSprite = new Sprite(CoreEngine.getCurrent().getGame().getCursor().getTexture());
			}
			else
			{
				cursorSprite.setTexture(CoreEngine.getCurrent().getGame().getCursor().getTexture());
			}
			cursorSprite.render(shader, transform, hudCam, delta, renderEngine);
		}
		renderEngine.popState();
	}

	public boolean receivesShadows()
	{
		return false;
	}
}
