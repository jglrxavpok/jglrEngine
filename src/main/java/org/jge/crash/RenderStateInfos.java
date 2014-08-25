package org.jge.crash;

import java.util.Iterator;
import java.util.Map.Entry;

import org.jge.CoreEngine;
import org.jge.render.RenderState;
import org.jge.util.OpenGLUtils;

public class RenderStateInfos extends CrashInfos
{

	@Override
	public String toString()
	{
		String renderState = SECTION_START + " Current RenderState "+SECTION_END;
		RenderState current = CoreEngine.getCurrent().getRenderEngine().getRenderState();
		Iterator<Integer> it = current.getGLCaps().keySet().iterator();
		String glCaps = "";
		while(it.hasNext())
		{
			int cap = it.next();
			boolean isEnabled = current.getGLCaps().get(cap);
			glCaps+="\tglCaps["+OpenGLUtils.getCapName(cap)+"] = "+isEnabled+"\n";
		}
		String funcs = "\tAlpha func: "+current.getAlphaFunc()+", ref: "+current.getAlphaRef()+"\n";
		funcs+="\tBlend Func src: "+current.getBlendFuncSrc()+", Blend func dst: "+current.getBlendFuncDst()+"\n";
		Iterator<Entry<String, Integer>> it1 = current.getInts().entrySet().iterator();
		String intEntries = "\t"+current.getInts().entrySet().size()+" int entries: "+"\n";
		while(it1.hasNext())
		{
			Entry<String, Integer> entry = it1.next();
			intEntries+="\tInt entry: "+entry.getKey()+" = "+entry.getValue()+"\n";
		}
		return renderState+"\n"+glCaps+funcs+intEntries.substring(0, intEntries.length()-1);
	}

}
