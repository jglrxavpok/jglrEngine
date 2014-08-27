package org.jge.render;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.jge.CoreEngine;
import org.jge.ResourceLocation;
import org.jge.components.Camera;
import org.jge.components.SceneObject;
import org.jge.maths.Matrix4;
import org.jge.maths.Vector3;
import org.jge.render.mesh.Mesh;
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
	private Mesh				 mesh;
	private ArrayList<Vertex>	vertices		 = new ArrayList<Vertex>();
	private ArrayList<Integer>   indices		  = new ArrayList<Integer>();
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

		mesh = new Mesh();
	}

	public void onAddToScene(SceneObject object)
	{
		billboardRenderer = new BillboardSprite((Sprite)null);
		addChildAs("billboardRenderer", billboardRenderer);
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
		// shader.bind();
		// shader.updateUniforms(getTransform(), cam, particleMaterial, engine);

		int currentIndex = 0;
		sortParticles(cam);
		for(Particle particle : particles)
		{
			Sprite s = particle.getSprite();
			s.setX(-0.5);
			s.setY(-0.5);
			s.setWidth(1);
			s.setHeight(1);
			billboardRenderer.setSprite(s);
			billboardRenderer.getTransform().setPosition(new Vector3(particle.getPos().x, particle.getPos().y, particle.getPos().z));
			// Transform trans = particle.getTransform();
			// double yAngle =
			// Maths.acos(cam.getPos().copy().setY(0).normalize().dot(particle.getPos().copy().setY(0).normalize()));
			// trans.setRotation(new Quaternion(new Vector3(0, 1, 0), yAngle));
			// currentIndex += s.prepareGroupedRendering(vertices, indices,
			// currentIndex, particle.getPos().x, particle.getPos().y,
			// particle.getPos().z, trans);
			billboardRenderer.render(shader, cam, delta, engine);
		}

		// Integer[] indicesArray = indices.toArray(new Integer[0]);
		// Vertex[] verticesArray = vertices.toArray(new Vertex[0]);
		// int[] indicesArrayInt = new int[indicesArray.length];
		// for(int i = 0; i < indicesArray.length; i++ )
		// indicesArrayInt[i] = indicesArray[i];
		// mesh.setVertices(verticesArray, indicesArrayInt, false);
		// mesh.sendDataToOGL();
		// mesh.draw();

		vertices.clear();
		indices.clear();

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
