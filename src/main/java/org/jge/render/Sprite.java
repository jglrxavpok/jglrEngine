package org.jge.render;

import java.util.ArrayList;

import org.jge.components.Camera;
import org.jge.maths.Maths;
import org.jge.maths.Transform;
import org.jge.maths.Vector2;
import org.jge.maths.Vector3;
import org.jge.render.mesh.Mesh;
import org.jge.render.shaders.Shader;

public class Sprite extends TextureRegion
{

	private float	x;
	private float	y;
	private float	w;
	private float	h;
	private Mesh	 mesh;
	private Material material;
	private boolean  stateChanged   = true;
	private Vector2  topRightUV	 = new Vector2(getMaxU(), getMaxV());
	private Vector2  bottomRightUV  = new Vector2(getMaxU(), getMinV());
	private Vector2  topLeftUV	  = new Vector2(getMinU(), getMaxV());
	private Vector2  bottomLeftUV   = new Vector2(getMinU(), getMinV());

	private Vector3  topRightPos	= Vector3.NULL.copy();
	private Vector3  bottomRightPos = Vector3.NULL.copy();
	private Vector3  topLeftPos	 = Vector3.NULL.copy();
	private Vector3  bottomLeftPos  = Vector3.NULL.copy();

	public Sprite(TextureRegion region)
	{
		this(region.getTexture(), region.getMinU(), region.getMinV(), region.getMaxU(), region.getMaxV(), Maths.abs(region.getMaxU() - region.getMinU()) * region.getTexture().getWidth(), Maths.abs(region.getMaxV() - region.getMinV()) * region.getTexture().getHeight());
	}

	public Sprite(Texture texture, double pixelX, double pixelY, double pixelW, double pixelH)
	{
		this(texture, (double)pixelX / (double)texture.getWidth(), (double)pixelY / (double)texture.getHeight(), ((double)pixelX + (double)pixelW) / (double)texture.getWidth(), ((double)pixelY + (double)pixelH) / (double)texture.getHeight(), pixelW, pixelH);
	}

	public Sprite(Texture texture, double pixelW, double pixelH)
	{
		this(texture, 0, 0, 1, 1, pixelW, pixelH);
	}

	public Sprite(Texture texture, double minU, double minV, double maxU, double maxV, double pixelW, double pixelH)
	{
		super(texture, minU, minV, maxU, maxV);
		this.w = (float)pixelW;
		this.h = (float)pixelH;
		this.mesh = new Mesh();
		this.material = new Material();
		material.setTexture("diffuse", texture);
	}

	public Sprite setRegionMinX(double x)
	{
		this.setMinU(x / (double)getTexture().getWidth());
		return this;
	}

	public Sprite setRegionMaxX(double x)
	{
		this.setMaxU(x / (double)getTexture().getWidth());
		return this;
	}

	public Sprite setRegionMinY(double y)
	{
		this.setMinV(y / (double)getTexture().getHeight());
		return this;
	}

	public Sprite setRegionMaxY(double y)
	{
		this.setMaxV(y / (double)getTexture().getHeight());
		return this;
	}

	public double getRegionMinX()
	{
		return getMinU() * (double)getTexture().getWidth();
	}

	public double getRegionMaxX()
	{
		return getMaxU() * (double)getTexture().getWidth();
	}

	public double getRegionMinY()
	{
		return getMinV() * (double)getTexture().getHeight();
	}

	public double getRegionMaxY()
	{
		return getMaxV() * (double)getTexture().getHeight();
	}

	public Sprite(Texture texture)
	{
		this(texture, 0, 0, 1, 1, texture.getWidth(), texture.getHeight());
	}

	public int prepareGroupedRendering(ArrayList<Vertex> vertices, ArrayList<Integer> indices, int currentIndex, float x, float y)
	{
		return prepareGroupedRendering(vertices, indices, currentIndex, x, y, 0);
	}

	public int prepareGroupedRendering(ArrayList<Vertex> vertices, ArrayList<Integer> indices, int currentIndex, float x, float y, float z)
	{
		return prepareGroupedRendering(vertices, indices, currentIndex, x, y, z, Transform.NULL);
	}

	public int prepareGroupedRendering(ArrayList<Vertex> vertices, ArrayList<Integer> indices, int currentIndex, float x, float y, float z, Transform trans)
	{
		Vector3 a = trans.transform(topLeftPos.set(x, y, z));
		Vector3 b = trans.transform(topRightPos.set(x + w, y, z));
		Vector3 c = trans.transform(bottomRightPos.set(x + w, y + h, z));
		Vector3 d = trans.transform(bottomLeftPos.set(x, y + h, z));
		vertices.add(new Vertex(a, topLeftUV.set(getMinU(), getMaxV())));
		vertices.add(new Vertex(b, topRightUV.set(getMaxU(), getMaxV())));
		vertices.add(new Vertex(c, bottomRightUV.set(getMaxU(), getMinV())));
		vertices.add(new Vertex(d, bottomLeftUV.set(getMinU(), getMinV())));
		indices.add(currentIndex + 0);
		indices.add(currentIndex + 2);
		indices.add(currentIndex + 3);

		indices.add(currentIndex + 0);
		indices.add(currentIndex + 1);
		indices.add(currentIndex + 2);
		return 4;
	}

	public Sprite prepareRendering(float x, float y)
	{
		Vertex[] vertices = new Vertex[]
		{
				new Vertex(topLeftPos.set(x, y, 0), topLeftUV.set(getMinU(), getMaxV())), new Vertex(topRightPos.set(x + w, y, 0), topRightUV.set(getMaxU(), getMaxV())), new Vertex(bottomRightPos.set(x + w, y + h, 0), bottomRightUV.set(getMaxU(), getMinV())), new Vertex(bottomLeftPos.set(x, y + h, 0), bottomLeftUV.set(getMinU(), getMinV()))
		};
		int[] indices = new int[]
		{
				0, 2, 3, 0, 1, 2
		};
		mesh.setVertices(vertices, indices);
		mesh.sendDataToOGL();
		return this;
	}

	public Sprite render(Shader shader, Transform transform, Camera cam, double delta, RenderEngine engine)
	{
		if(stateChanged)
		{
			prepareRendering(getX(), getY());
			this.stateChanged = false;
		}
		shader.bind();
		shader.updateUniforms(transform, cam, material, engine);
		mesh.draw();
		return this;
	}

	public float getY()
	{
		return y;
	}

	public float getX()
	{
		return x;
	}

	public Sprite setY(float y)
	{
		this.y = y;
		this.stateChanged = true;
		return this;
	}

	public Sprite setX(float x)
	{
		this.x = x;
		this.stateChanged = true;
		return this;
	}

	public float getHeight()
	{
		return h;
	}

	public float getWidth()
	{
		return w;
	}

	public Sprite setHeight(float h)
	{
		this.h = h;
		this.stateChanged = true;
		return this;
	}

	public Sprite setWidth(float w)
	{
		this.w = w;
		this.stateChanged = true;
		return this;
	}
}
