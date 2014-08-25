package org.jge.commands;

import org.jge.CoreEngine;
import org.jge.maths.Vector3;

public class ShadowColorCommand extends AbstractCommand
{

	@Override
	public String getCommand()
	{
		return "cl_shadowColor";
	}

	@Override
	public String run(CommandArgument[] args)
	{
		if(args.length != 3)
			return BAD_USAGE;
		CoreEngine.getCurrent().getRenderEngine().setVector3("shadowColor", new Vector3(args[0].getContentAsDouble(), args[1].getContentAsDouble(), args[2].getContentAsDouble()));
		return "Successfully changed shadow color";
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
					getCommand()+" <red> <green> <blue>"
				};
	}

	public String getDescription()
	{
		return "Set the color of shadows";
	}
}
