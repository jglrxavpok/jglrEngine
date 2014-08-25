package org.jge.render.mesh.constants;

import org.jge.JGEngine;
import org.jge.ResourceLocation;
import org.jge.render.mesh.Mesh;

public class PlaneMesh extends Mesh
{

	public PlaneMesh() throws Exception
	{
		super(JGEngine.getResourceLoader().getResource(new ResourceLocation("models", "plane.obj")));
	}

}
