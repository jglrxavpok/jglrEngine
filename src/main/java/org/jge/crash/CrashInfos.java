package org.jge.crash;

@FunctionalInterface
public interface CrashInfos
{

	public static final String SECTION_START = "[=====";
	public static final String SECTION_END   = "=====]";

	public String getInfos();
}
