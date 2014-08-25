package org.jge.crash;

import org.lwjgl.openal.AL10;

public class OpenALInfos extends CrashInfos
{

	@Override
	public String toString()
	{
		String s = SECTION_START+" OpenAL "+SECTION_END;
		s+="\n\tVersion: "+AL10.alGetString(AL10.AL_VERSION);
		s+="\n\tVendor: "+AL10.alGetString(AL10.AL_VENDOR);
		return s;
	}

}
