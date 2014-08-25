package org.jge.crash;

import java.nio.charset.Charset;

import org.jge.JGEngine;
import org.jge.ResourceLocation;
import org.jge.maths.Maths;
import org.jge.util.Log;

public class CrashReport
{

	private static String[] comments = new String[]
			{
				null,
				null,
				"Well, this was a disappointment."
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
		if(!Log.save)
			Log.save = true; // Force output
		StringBuffer buffer = new StringBuffer();
		buffer.append(CrashInfos.SECTION_START+" Crash "+CrashInfos.SECTION_END+"\n");
		String comment = generateRandomComment();
		buffer.append(comment+"\n");
		buffer.append("\n\t"+exception.getClass().getCanonicalName());
		StackTraceElement[] stackTrace = exception.getStackTrace();
		if(exception.getLocalizedMessage() != null)
		{
			buffer.append(" reason: "+exception.getLocalizedMessage());
		}
		else if(exception.getMessage() != null)
			buffer.append(" reason: "+exception.getMessage());
		buffer.append("\n");
		if(stackTrace != null && stackTrace.length > 0)
		{
			for(StackTraceElement elem : stackTrace)
			{
				buffer.append("\t\t"+elem.toString()+"\n");
			}
		}
		else
		{
			buffer.append("\t**** Stack Trace is empty ****");
		}
		buffer.append(new DateInfos()+"\n");
		buffer.append(new OSInfos()+"\n");
		buffer.append(new OpenALInfos()+"\n");
		buffer.append(new OpenGLInfos()+"\n");
		buffer.append(new RenderStateInfos()+"\n");
		Log.error(buffer.toString(), false);
	}

	private String generateRandomComment()
	{
		return comments[(int)Maths.floor(Maths.rand()*comments.length)];
	}

}
