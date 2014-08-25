package org.jge.commands;

import javax.vecmath.Vector3f;

import org.jge.CoreEngine;

public class SetGravityCommand extends AbstractCommand
{

	@Override
	public String getCommand()
	{
		return "gravity";
	}
	
	@Override
	public String run(CommandArgument[] args)
	{
		if(args.length == 3)
			CoreEngine.getCurrent().getPhysicsEngine().getWorld().setGravity(new Vector3f(args[0].getContentAsFloat(), -args[1].getContentAsFloat(), args[2].getContentAsFloat()));
		else if(args.length == 1)
		{
			CoreEngine.getCurrent().getPhysicsEngine().getWorld().setGravity(new Vector3f(0, -args[0].getContentAsFloat(), 0));
		}
		else
			return BAD_USAGE;
		return "Successfully changed gravity";
	}

	@Override
	public int getPermissionLevel()
	{
		return MODERATOR;
	}

	public String[] getCorrectUsages()
	{
		return new String[]
				{
			getCommand()+" <forceOnAxisX> <forceOnAxisY> <forceOnAxisZ>",
			getCommand()+" <force>"
				};
	}
	
	public String getDescription()
	{
		return "Sets the gravity in the world";
	}
}
