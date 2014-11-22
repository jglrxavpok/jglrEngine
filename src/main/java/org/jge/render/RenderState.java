package org.jge.render;

import static org.lwjgl.opengl.GL11.*;

import java.util.HashMap;
import java.util.Iterator;

import org.jge.gpuresources.MappedValues;
import org.jge.maths.Matrix4;

public class RenderState extends MappedValues implements Cloneable
{

	private HashMap<Integer, Boolean> glCaps = new HashMap<Integer, Boolean>();
	private Matrix4				   lightMatrix;
	private int					   blendFuncSrc;
	private int					   blendFuncDst;
	private int					   alphaFunc;
	private float					 alphaRef;
	private float					 clearColorR;
	private float					 clearColorG;
	private float					 clearColorB;
	private float					 clearColorA;

	public RenderState clone()
	{
		RenderState copy = new RenderState();
		copy.blendFuncSrc = blendFuncSrc;
		copy.blendFuncDst = blendFuncDst;
		copy.alphaFunc = alphaFunc;
		copy.alphaRef = alphaRef;
		copy.glCaps.putAll(glCaps);
		copy.lightMatrix = lightMatrix;
		copy.clearColorA = clearColorA;
		copy.clearColorR = clearColorR;
		copy.clearColorG = clearColorG;
		copy.clearColorB = clearColorB;
		copyInto(copy);
		return copy;
	}

	public float getAlphaRef()
	{
		return alphaRef;
	}

	public int getAlphaFunc()
	{
		return alphaFunc;
	}

	public int getBlendFuncSrc()
	{
		return blendFuncSrc;
	}

	public int getBlendFuncDst()
	{
		return blendFuncDst;
	}

	public HashMap<Integer, Boolean> getGLCaps()
	{
		return glCaps;
	}

	public RenderState copyInto(MappedValues map)
	{
		Iterator<String> keys = this.getInts().keySet().iterator();
		while(keys.hasNext())
		{
			String key = keys.next();
			map.setInt(key, getInt(key));
		}

		keys = this.getFloats().keySet().iterator();
		while(keys.hasNext())
		{
			String key = keys.next();
			map.setFloat(key, getFloat(key));
		}

		keys = this.getBooleans().keySet().iterator();
		while(keys.hasNext())
		{
			String key = keys.next();
			map.setBoolean(key, getBoolean(key));
		}

		keys = this.getTextures().keySet().iterator();
		while(keys.hasNext())
		{
			String key = keys.next();
			map.setTexture(key, getTexture(key));
		}

		keys = this.getVector3s().keySet().iterator();
		while(keys.hasNext())
		{
			String key = keys.next();
			map.setVector3(key, getVector3(key).copy());
		}
		return this;
	}

	public RenderState setGLCap(int cap, boolean enabled)
	{
		glCaps.put(cap, enabled);
		return this;
	}

	public void apply(RenderEngine renderEngine)
	{
		RenderState current = renderEngine.getRenderState();
		if(current != this)
		{
			Iterator<Integer> itCurrent = current.glCaps.keySet().iterator();
			while(itCurrent.hasNext())
			{
				int cap = itCurrent.next();
				if(current.glCaps.get(cap) && !glCaps.containsKey(cap)) glCaps.put(cap, false);
			}
			Iterator<Integer> it = glCaps.keySet().iterator();
			while(it.hasNext())
			{
				int cap = it.next();
				if(glCaps.get(cap))
					glEnable(cap);
				else
					glDisable(cap);
			}
			copyInto(renderEngine);
			glBlendFunc(blendFuncSrc, blendFuncDst);
			glAlphaFunc(alphaFunc, alphaRef);
			if(lightMatrix != null) renderEngine.getLightMatrix().set(lightMatrix);

			glClearColor(clearColorR, clearColorG, clearColorG, clearColorA);
		}
	}

	public void setLightMatrix(Matrix4 lightMatrix)
	{
		this.lightMatrix = lightMatrix;
	}

	public void setBlendFunc(int src, int dst)
	{
		this.blendFuncSrc = src;
		this.blendFuncDst = dst;
	}

	public void setAlphaFunc(int func, float ref)
	{
		this.alphaFunc = func;
		this.alphaRef = ref;
	}

	public void setClearColor(float r, float g, float b, float a)
	{
		this.clearColorR = r;
		this.clearColorG = g;
		this.clearColorB = b;
		this.clearColorA = a;
	}
}
