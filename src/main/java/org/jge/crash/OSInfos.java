package org.jge.crash;

public class OSInfos extends CrashInfos
{

	@Override
	public String toString()
	{
		String osName = System.getProperty("os.name");
		String osVersion = System.getProperty("os.version");
		return SECTION_START + " Operating System " + SECTION_END + "\n\tName: " + osName + "\n\tVersion: " + osVersion;
	}

}
