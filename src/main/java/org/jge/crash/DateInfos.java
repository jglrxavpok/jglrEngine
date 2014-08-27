package org.jge.crash;

import org.jge.Time;

public class DateInfos extends CrashInfos
{

	@Override
	public String toString()
	{
		return SECTION_START + " Date " + SECTION_END + "\n\t" + Time.getTimeAsText();
	}

}
