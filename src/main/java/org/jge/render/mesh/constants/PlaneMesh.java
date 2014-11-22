package org.jge.render.mesh.constants;

import org.jge.JGEngine;
import org.jge.ResourceLocation;
import org.jge.render.mesh.Mesh;

public class PlaneMesh extends Mesh
{

	public PlaneMesh() throws Exception
	{
		super(JGEngine.getClasspathResourceLoader().getResource(new ResourceLocation("test/models/plane.obj")));
	}

}
