package org.jge.crash;

import java.nio.charset.Charset;

import org.jge.CoreEngine;
import org.jge.JGEngine;
import org.jge.ResourceLocation;
import org.jge.maths.Maths;
import org.jge.util.Log;

public class CrashReport
{

	private static String[] comments = new String[]
									 {
			null, null, "Well, this was a disappointment.", "I'm sorry Dave. I think I can't let you do that", "Here, have a gift http://xkcd.com/953/ "
									 };

	static
	{
		try
		{
			comments[0] = new String(JGEngine.getResourceLoader().getResource(new ResourceLocation("text:crackedFloppy.ascii")).getData(), Charset.forName("utf-8")).replace("\n       -jglrxavpok", "");
			comments[1] = new String(JGEngine.getResourceLoader().getResource(new ResourceLocation("text:deadFace.ascii")).getData(), Charset.forName("utf-8")).replace("\n       -jglrxavpok", "");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static class UndefinedException extends Exception
	{

		public UndefinedException(String message)
		{
			super(message);
		}

		private static final long serialVersionUID = 3352250643266742630L;
	}

	private Throwable exception;

	public CrashReport(String message)
	{
		this(new UndefinedException(message).fillInStackTrace());
	}

	public CrashReport(Throwable throwable)
	{
		this.exception = throwable;
	}

	public void printStack()
	{
		if(!Log.save) Log.save = true; // Force output
		StringBuffer buffer = new StringBuffer();
		buffer.append(CrashInfos.SECTION_START + " Crash " + CrashInfos.SECTION_END + "\n");
		String comment = generateRandomComment();
		buffer.append(comment + "\n");
		buffer.append("\n" + exception.getClass().getCanonicalName());
		StackTraceElement[] stackTrace = exception.getStackTrace();
		if(exception.getLocalizedMessage() != null)
		{
			buffer.append(" reason: " + exception.getLocalizedMessage());
		}
		else if(exception.getMessage() != null) buffer.append(" reason: " + exception.getMessage());
		buffer.append("\n");
		if(stackTrace != null && stackTrace.length > 0)
		{
			for(StackTraceElement elem : stackTrace)
			{
				buffer.append("\tat " + elem.toString() + "\n");
			}
		}
		else
		{
			buffer.append("\t**** Stack Trace is empty ****");
		}
		add(buffer, () -> CrashInfos.SECTION_START + " Game " + CrashInfos.SECTION_END + "\n\tName: " + CoreEngine.getCurrent().getGame().getGameName());
		add(buffer, new DateInfos());
		add(buffer, new OSInfos());
		add(buffer, new OpenALInfos());
		add(buffer, new OpenGLInfos());
		add(buffer, new RenderStateInfos());
		System.err.println(buffer.toString());
	}

	private void add(StringBuffer buffer, CrashInfos infos)
	{
		buffer.append(infos.getInfos() + "\n");
	}

	private String generateRandomComment()
	{
		return comments[(int)Maths.floor(Maths.rand() * comments.length)];
	}

}
