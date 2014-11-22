package org.jge;

import java.util.HashMap;

import org.jge.commands.AbstractCommand;
import org.jge.commands.CommandArgument;
import org.jge.commands.HelpCommand;
import org.jge.commands.SetGravityCommand;
import org.jge.commands.ShadowColorCommand;
import org.jge.commands.UniformCommand;
import org.jge.util.Log;

public class CommandHelper
{

	public static HashMap<String, AbstractCommand> commands = new HashMap<String, AbstractCommand>();

	static
	{
		registerCommand(new HelpCommand());
		registerCommand(new ShadowColorCommand());
		registerCommand(new SetGravityCommand());
		registerCommand(new UniformCommand());
	}

	public static void registerCommand(AbstractCommand command)
	{
		commands.put(command.getCommand(), command);
	}

	public static boolean executeCommand(AbstractCommand command, CommandArgument[] args)
	{
		String result = command.run(args);

		if(result == null) return false;
		if(!result.equals(""))
		{
			if(result.startsWith(AbstractCommand.BAD_USAGE))
			{
				Log.error(result);
				if(command.getCorrectUsages() == null)
				{
					Log.error("The command: \"" + command.getCommand() + "\" has no correct usage bound to it yet, sorry ;(");
					return false;
				}
				String[] usages = command.getCorrectUsages();
				String usagesAsList = "";
				for(int i = 0; i < usages.length; i++ )
				{
					if(i != 0) usagesAsList += ", OR ";
					usagesAsList += "\"" + usages[i] + "\"";
				}
				Log.error("Correct usages are: [" + usagesAsList + "]");
				return false;
			}
			Log.message(result);
		}
		return true;
	}

	public static boolean executeStringCommand(String line)
	{
		String[] tokens = line.split(" ");

		if(commands.containsKey(tokens[0]))
		{
			AbstractCommand command = commands.get(tokens[0]);
			CommandArgument[] args = new CommandArgument[tokens.length - 1];
			for(int i = 1; i < tokens.length; i++ )
			{
				args[i - 1] = new CommandArgument(tokens[i]);
			}
			return executeCommand(command, args);
		}

		return false;
	}

}
