package org.jge.render;

import org.jge.JGEngine;
import org.jge.ResourceLocation;
import org.jge.RuntimeEngineException;
import org.jge.gpuresources.MappedValues;

public class Material extends MappedValues
{

	public static final Material defaultMaterial = new Material();

	public Material()
	{
		super();
		try
		{
			getTextures().setDefault(new Texture(JGEngine.getResourceLoader().getResource(new ResourceLocation("textures", "damier.png"))));
			setTexture("normalMap", new Texture(JGEngine.getResourceLoader().getResource(new ResourceLocation("textures", "default_normal.png"))));
			setTexture("dispMap", new Texture(JGEngine.getResourceLoader().getResource(new ResourceLocation("textures", "default_disp.png"))));
			setTexture("lightMap", new Texture(JGEngine.getResourceLoader().getResource(new ResourceLocation("textures", "default_light.png"))));
			setFloat("dispMapScale", 0.04f);

			float baseBias = getFloat("dispMapScale") / 2.0f;
			float dispMapOffset = -1.0f;
			setFloat("dispMapBias", -baseBias + baseBias * dispMapOffset);
		}
		catch(Exception e)
		{
			throw new RuntimeEngineException("Error while loading material", e);
		}
	}
}
