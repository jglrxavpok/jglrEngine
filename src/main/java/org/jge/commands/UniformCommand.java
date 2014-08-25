package org.jge.commands;

import org.jge.CoreEngine;
import org.jge.maths.Vector2;
import org.jge.maths.Vector3;
import org.jge.render.shaders.Shader;
import org.jge.util.Log;

public class UniformCommand extends AbstractCommand
{

	@Override
	public String getCommand()
	{
		return "cl_uniform";
	}

	@Override
	public String run(final CommandArgument[] args)
	{
		if(args.length < 3)
		{
			return BAD_USAGE;
		}
		for(final Shader s : Shader.loadedShaders)
		{
			if(args[0].getContentAsString().equals(s.getResource().getBindName()))
			{
				CoreEngine.getCurrent().getRenderEngine().invokeBeforeRender(new Runnable()
				{
					public void run()
					{
						String uniformName = "S_"+args[1].getContentAsString();
						String[] values = new String[args.length-2];
						for(int i = 2;i<args.length;i++)
						{
							values[i-2] = args[i].getContentAsString();
						}
						s.bind();
						String uniformType = s.getUniformType(uniformName);
						if(uniformType == null)
						{
							Log.error("The type of uniform "+uniformName+" is undefined, the uniform might not exist!");
							return;
						}
						if(!s.isStruct(uniformType))
						{
							if(uniformType.equals("int"))
							{
								s.setUniformi(uniformName, Integer.parseInt(values[0]));
								Log.message("Successfully updated "+uniformType+" "+uniformName+" in shader "+args[0].getContentAsString());
							}
							else if(uniformType.equals("float"))
							{
								s.setUniformf(uniformName, Float.parseFloat(values[0]));
								Log.message("Successfully updated "+uniformType+" "+uniformName+" in shader "+args[0].getContentAsString());
							}
							else if(uniformType.equals("bool"))
							{
								s.setUniformb(uniformName, Boolean.parseBoolean(values[0]));
								Log.message("Successfully updated "+uniformType+" "+uniformName+" in shader "+args[0].getContentAsString());
							}
							else if(uniformType.equals("vec2"))
							{
								s.setUniform(uniformName, new Vector2(Double.parseDouble(values[0]), Double.parseDouble(values[1])));
								Log.message("Successfully updated "+uniformType+" "+uniformName+" in shader "+args[0].getContentAsString());
							}
							else if(uniformType.equals("vec3"))
							{
								s.setUniform(uniformName, new Vector3(Double.parseDouble(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2])));
								Log.message("Successfully updated "+uniformType+" "+uniformName+" in shader "+args[0].getContentAsString());
							}
							else
							{
								Log.error("Uniform "+uniformName+" wasn't found in "+args[0].getContentAsString());
							}
						}
						else
						{
							Log.error("Uniform "+uniformName+" is a struct and can't be modified");
						}
					}
				});
				return "Successfully sent uniform infos to render engine";
			}
		}
		
		return "Unknown shader "+args[0].getContentAsString();
	}

	@Override
	public int getPermissionLevel()
	{
		return 1;
	}
	
	public String[] getCorrectUsages()
	{
		return new String[]
				{
				"cl_uniform <shaderName> <uniformName> <values>"
				};
	}

	public String getDescription()
	{
		return "Set a field of a specific shader";
	}
}
