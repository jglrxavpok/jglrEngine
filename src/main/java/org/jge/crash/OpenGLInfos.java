package org.jge.crash;

import org.jge.util.OpenGLUtils;

public class OpenGLInfos implements CrashInfos
{

	@Override
	public String getInfos()
	{
		String header = SECTION_START + " OpenGL " + SECTION_END;
		return header + "\n\tVersion: " + OpenGLUtils.getOpenGLVersion() + "\n\tVendor: " + OpenGLUtils.getOpenGLVendor();
	}

}
