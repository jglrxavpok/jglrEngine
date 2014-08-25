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

	private double x;
	private double y;
	private double w;
	private double h;
	private Mesh mesh;
	private Material material;
	private boolean stateChanged = true;
	
	public Sprite(TextureRegion region)
	{
		this(region.getTexture(), 
				region.getMinU(),
				region.getMinV(),
				region.getMaxU(),
				region.getMaxV(), 
				Maths.abs(region.getMaxU()-region.getMinU())/region.getTexture().getWidth(),
				Maths.abs(region.getMaxV()-region.getMinV())/region.getTexture().getHeight());
	}

	public Sprite(Texture texture, double pixelX, double pixelY, double pixelW, double pixelH)
	{
		this(texture, (double)pixelX/(double)texture.getWidth(), (double)pixelY/(double)texture.getHeight(), ((double)pixelX+(double)pixelW)/(double)texture.getWidth(), ((double)pixelY+(double)pixelH)/(double)texture.getHeight(), pixelW, pixelH);
	}
	
	public Sprite(Texture texture, double pixelW, double pixelH)
	{
		this(texture, 0, 0, 1, 1, pixelW, pixelH);
	}
	
	public Sprite(Texture texture, double minU, double minV, double maxU, double maxV, double pixelW, double pixelH)
	{
		super(texture, minU, minV, maxU, maxV);
		this.w = pixelW;
		this.h = pixelH;
		this.mesh = new Mesh();
		this.material = new Material();
		material.setTexture("diffuse", texture);
	}
	
	public Sprite(Texture texture)
	{
		this(texture, 0,0,1,1, texture.getWidth(), texture.getHeight());
	}

	public int prepareGroupedRendering(ArrayList<Vertex> vertices, ArrayList<Integer> indices, int currentIndex, double x, double y)
	{
		return prepareGroupedRendering(vertices, indices, currentIndex, x, y, 0);
	}
	
	
	public int prepareGroupedRendering(ArrayList<Vertex> vertices, ArrayList<Integer> indices, int currentIndex, double x, double y, double z)
	{
		return prepareGroupedRendering(vertices, indices, currentIndex, x, y, z, Transform.NULL);
	}
	
	public int prepareGroupedRendering(ArrayList<Vertex> vertices, ArrayList<Integer> indices, int currentIndex, double x, double y, double z, Transform trans)
	{
		Vector3 a = trans.transform(new Vector3(x, y, z));
		Vector3 b = trans.transform(new Vector3(x+w, y, z));
		Vector3 c = trans.transform(new Vector3(x+w, y+h, z));
		Vector3 d = trans.transform(new Vector3(x, y+h, z));
		vertices.add(new Vertex(a, new Vector2(getMinU(), getMaxV())));
		vertices.add(new Vertex(b, new Vector2(getMaxU(), getMaxV())));
		vertices.add(new Vertex(c, new Vector2(getMaxU(), getMinV())));
		vertices.add(new Vertex(d, new Vector2(getMinU(), getMinV())));
		indices.add(currentIndex + 0);
		indices.add(currentIndex + 2);
		indices.add(currentIndex + 3);
		
		indices.add(currentIndex + 0);
		indices.add(currentIndex + 1);
		indices.add(currentIndex + 2);
		return 4;
	}
	
	public Sprite prepareRendering(double x, double y)
	{
		Vertex[] vertices = new Vertex[]
				{
				new Vertex(new Vector3(x, y, 0), new Vector2(getMinU(), getMaxV())),
				new Vertex(new Vector3(x+w, y, 0), new Vector2(getMaxU(), getMaxV())),
				new Vertex(new Vector3(x+w, y+h, 0), new Vector2(getMaxU(), getMinV())),
				new Vertex(new Vector3(x, y+h, 0), new Vector2(getMinU(), getMinV()))
				};
		int[] indices = new int[]
				{
				0,2,3,
				0,1,2
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

	public double getY()
	{
		return y;
	}

	public double getX()
	{
		return x;
	}
	
	public Sprite setY(double y)
	{
		this.y = y;
		this.stateChanged = true;
		return this;
	}
	
	public Sprite setX(double x)
	{
		this.x = x;
		this.stateChanged = true;
		return this;
	}
	
	public double getHeight()
	{
		return h;
	}

	public double getWidth()
	{
		return w;
	}
	
	public Sprite setHeight(double h)
	{
		this.h = h;
		this.stateChanged = true;
		return this;
	}
	
	public Sprite setWidth(double w)
	{
		this.w = w;
		this.stateChanged = true;
		return this;
	}
}
