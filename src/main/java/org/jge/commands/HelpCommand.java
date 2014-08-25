package org.jge.commands;

import java.util.Iterator;

import org.jge.Console;
import org.jge.util.Log;

public class HelpCommand extends AbstractCommand
{

	@Override
	public String getCommand()
	{
		return "help";
	}

	@Override
	public String run(CommandArgument[] args)
	{
		Log.message("*** Help ***");
		if(args.length == 0)
		{
			Iterator<String> it = Console.commands.keySet().iterator();
			while(it.hasNext())
			{
				String commandName = it.next();
				AbstractCommand command = Console.commands.get(commandName);
				String usagesList = "";
				String[] usages = command.getCorrectUsages();
				if(usages != null)
    				for(int i = 0;i<usages.length;i++)
    				{
    					if(i != 0)
    						usagesList +=", OR ";
    					usagesList +="\""+usages[i]+"\"";
    				}
				else
				{
					usagesList = "No correct usage bound, that's sad ;(";
				}
				String desc = command.getDescription();
				Log.message("Command \""+commandName+"\" usages: "+usagesList+ (desc == null ? "" : " Description: "+desc));
				
			}
			return null;
		}
		else if(args.length == 1)
		{
			String commandName = args[0].getContentAsString();
			AbstractCommand command = Console.commands.get(commandName);
			if(command == null)
			{
				return "Unknown command "+commandName;
			}
			else
			{
				String usagesList = "";
				String[] usages = command.getCorrectUsages();
				if(usages != null)
    				for(int i = 0;i<usages.length;i++)
    				{
    					if(i != 0)
    						usagesList +=", OR ";
    					usagesList +="\""+usages[i]+"\"";
    				}
				else
				{
					usagesList = "No correct usage bound, that's sad ;(";
				}
				String desc = command.getDescription();
				return "Command \""+commandName+"\" usages: "+usagesList+ (desc == null ? "" : " Description: "+desc);
			}
		}
		else
		{
			return BAD_USAGE;
		}
	}

	@Override
	public int getPermissionLevel()
	{
		return USER;
	}
	
	public String[] getCorrectUsages()
	{
		return new String[]
				{
				"help", "help <command>"
				};
	}
	
	public String getDescription()
	{
		return "Prints the description of available commands";
	}

}
