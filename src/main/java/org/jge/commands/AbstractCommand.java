package org.jge.commands;

public abstract class AbstractCommand
{

	public static final String BAD_USAGE = "Bad usage of command";
	public static final int	ADMIN	 = 10;
	public static final int	SUBADMIN  = 9;

	public static final int	MODERATOR = 5;

	public static final int	SUPERUSER = 2;
	public static final int	USER	  = 1;

	public abstract String getCommand();

	public abstract String run(CommandArgument[] args);

	public abstract int getPermissionLevel();

	public String[] getCorrectUsages()
	{
		return null;
	}

	public String getDescription()
	{
		return null;
	}
}
