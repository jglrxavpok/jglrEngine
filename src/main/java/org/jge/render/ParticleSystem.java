package org.jge.render;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.jge.CoreEngine;
import org.jge.ResourceLocation;
import org.jge.components.Camera;
import org.jge.components.SceneComponent;
import org.jge.components.SceneObject;
import org.jge.maths.Matrix4;
import org.jge.maths.Vector3;
import org.jge.render.shaders.Shader;

public class ParticleSystem extends SceneObject
{

	public static final Material particleMaterial = new Material();
	static
	{
		try
		{
			particleMaterial.setTexture("diffuse", new Texture(CoreEngine.getCurrent().getClasspathResourceLoader().getResource(new ResourceLocation("textures", "particles.png")), GL_NEAREST));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private int				  max;
	private ArrayList<Particle>  particles;
	private ArrayList<Particle>  toRemove;
	private BillboardSprite	  billboardRenderer;

	public ParticleSystem()
	{
		this(2000);
	}

	public ParticleSystem(int max)
	{
		this.max = max;
		this.particles = new ArrayList<Particle>();
		this.toRemove = new ArrayList<Particle>();
	}

	public void onAddToScene(SceneObject object)
	{
		billboardRenderer = new BillboardSprite((Sprite)null);
		addChildAs("billboardRenderer", billboardRenderer);
	}

	public void renderAll(Shader shader, Camera cam, double delta, RenderEngine renderEngine)
	{
		render(shader, cam, delta, renderEngine);
		for(SceneComponent component : getComponents())
		{
			component.render(shader, cam, delta, renderEngine);
		}
		for(SceneObject child : getChildren())
		{
			if(child != billboardRenderer) child.renderAll(shader, cam, delta, renderEngine);
		}
	}

	public void update(double delta)
	{
		toRemove.clear();
		for(Particle p : particles)
		{
			if(p != null)
			{
				if(p.isDead()) toRemove.add(p);
				p.update(delta);
			}
		}
		if(!toRemove.isEmpty()) particles.removeAll(toRemove);
	}

	public void render(Shader shader, Camera cam, double delta, RenderEngine engine)
	{
		if(particles.isEmpty()) return;

		engine.pushState();

		engine.setLightMatrix(new Matrix4().initIdentity());
		engine.disableGLCap(GL_CULL_FACE);

		if(!engine.isRenderingShadowMap())
		{
			engine.enableGLCap(GL_ALPHA_TEST);
			engine.setAlphaFunc(GL_GEQUAL, 0.01f);
		}

		engine.setLighting(false);
		engine.setShadowing(false);
		engine.setParallaxDispMapping(false);
		engine.setBoolean("normalMapping", false);
		engine.setAmbientColor(1, 1, 1);

		sortParticles(cam);
		for(Particle particle : particles)
		{
			Sprite s = particle.getSprite();
			s.setX(-0.5f);
			s.setY(-0.5f);
			s.setWidth(1);
			s.setHeight(1);
			billboardRenderer.setSprite(s);
			billboardRenderer.getTransform().setPosition(Vector3.get(particle.getPos().x, particle.getPos().y, particle.getPos().z));
			billboardRenderer.render(shader, cam, delta, engine);
		}

		engine.popState();
	}

	private void sortParticles(final Camera cam)
	{
		Collections.sort(particles, new Comparator<Particle>()
		{
			public int compare(Particle a, Particle b)
			{
				return Double.compare(cam.getParent().getTransform().getTransformedPos().sub(a.getPos()).z, cam.getParent().getTransform().getTransformedPos().sub(b.getPos()).z);
			}
		});
		;
	}

	public ParticleSystem addParticle(Particle p)
	{
		particles.add(p);
		return this;
	}

}
