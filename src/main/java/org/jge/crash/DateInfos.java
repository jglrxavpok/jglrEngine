package org.jge.crash;

import org.jge.Time;

public class DateInfos implements CrashInfos
{

	@Override
	public String getInfos()
	{
		return SECTION_START + " Date " + SECTION_END + "\n\t" + Time.getTimeAsText();
	}

}
