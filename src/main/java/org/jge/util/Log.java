package org.jge.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

import org.jge.JGEngine;
import org.jge.ResourceLocation;
import org.jge.Time;
import org.jge.crash.CrashReport;

public class Log
{

	@Retention(RetentionPolicy.RUNTIME)
	public static @interface NonLoggable
	{
	}

	public static boolean	   showCaller = true;

	private static Formatter	formatter  = ((message) -> "[" + JGEngine.getGame().getGameName() + " " + Time.getTimeAsString() + (showCaller ? " in " + getCaller() : "") + "] " + message);
	private static OutputStream savingOutput;

	public static boolean	   save	   = false;
	private static File		 outputFile;

	static
	{
	}

	@NonLoggable
	public static void message(String msg)
	{
		message(msg, true);
	}

	@NonLoggable
	private static String getCaller()
	{
		StackTraceElement[] elems = Thread.currentThread().getStackTrace();
		if(elems != null)
		{
			elementsIteration: for(int i = 1; i < elems.length; i++ )
			{
				StackTraceElement elem = elems[i];
				if(elem.getClassName().contains(Log.class.getCanonicalName())) continue;
				try
				{
					Class<?> c = Class.forName(elem.getClassName());
					if(c.isAnnotationPresent(NonLoggable.class)) continue;
					for(Method method : c.getDeclaredMethods())
					{
						if(method.getName().equals(elem.getMethodName())) if(method.isAnnotationPresent(NonLoggable.class)) continue elementsIteration;
					}
					String s = elem.getClassName() + "." + elem.getMethodName() + ":" + elem.getLineNumber();
					return s;
				}
				catch(ClassNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		}
		return "Unknown source";
	}

	@NonLoggable
	public static void message(String msg, boolean format)
	{
		log("[INFO] " + msg, System.out, format);
	}

	@NonLoggable
	public static void error(String msg)
	{
		error(msg, true);
	}

	@NonLoggable
	public static void error(String msg, boolean format)
	{
		log("[ERROR] " + msg, System.err, format);
	}

	@NonLoggable
	private static void log(String msg, PrintStream out, boolean format)
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

	@NonLoggable
	public static void fatal(String string)
	{
		JGEngine.crash(new CrashReport(string));
	}
}
