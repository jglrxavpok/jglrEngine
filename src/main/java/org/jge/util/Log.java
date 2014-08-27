package org.jge.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.jge.JGEngine;
import org.jge.ResourceLocation;
import org.jge.Time;

public class Log
{

	private static Formatter	formatter  = new DefaultFormatter();
	private static OutputStream savingOutput;

	public static boolean	   save	   = false;
	private static File		 outputFile;
	private static long		 lastWarned = 0;

	static
	{
	}

	public static void message(String msg)
	{
		message(msg, true);
	}

	public static void message(String msg, boolean format)
	{
		log(msg, System.out, format);
	}

	public static void error(String msg)
	{
		error(msg, true);
	}

	public static void error(String msg, boolean format)
	{
		log(msg, System.err, format);
	}

	public static void log(String msg, PrintStream out)
	{
		log(msg, out, true);
	}

	public static void log(String msg, PrintStream out, boolean format)
	{
		String finalMessage = msg;
		if(format) finalMessage = formatter.format(msg);
		if(save)
		{
			try
			{
				if(outputFile == null || !outputFile.exists() || savingOutput == null)
				{
					outputFile = JGEngine.getDiskResourceLoader().getResourceOrCreate(new ResourceLocation(JGEngine.getGame().getGameFolder().getAbsolutePath(), "logs/log_" + Strings.createCorrectedFileName(Time.getTimeAsString()) + ".log")).asFile();
					savingOutput = new BufferedOutputStream(new FileOutputStream(outputFile));
				}
				savingOutput.write((finalMessage + "\n").getBytes());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			if(System.currentTimeMillis() - lastWarned > 15000)
			{
				lastWarned = System.currentTimeMillis();
				error("[GRAVE] ******* LOG SAVING IS OFF, MIGHT BE A MISTAKE, PLEASE REMEMBER TO CHECK ******* ");
			}
		}
		out.println(finalMessage);
	}

	public static void setSaveOutput(OutputStream output)
	{
		Log.savingOutput = output;
	}

	public static void finish() throws Exception
	{
		if(save)
		{
			savingOutput.flush();
			savingOutput.close();
		}
	}
}
