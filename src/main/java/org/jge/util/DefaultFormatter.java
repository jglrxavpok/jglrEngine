package org.jge.util;

import org.jge.JGEngine;
import org.jge.Time;

public class DefaultFormatter implements Formatter
{

	public String format(String message)
	{
		return "[" + JGEngine.getGame().getGameName() + " " + Time.getTimeAsString() + "] " + message;
	}

}
