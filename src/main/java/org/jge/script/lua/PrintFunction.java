package org.jge.script.lua;

import org.jge.util.Log;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.LibFunction;

/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 jglrxavpok
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class PrintFunction extends LibFunction
{

	private Globals globals;

	public PrintFunction(Globals globals)
	{
		this.globals = globals;
	}

	public LuaValue call(LuaValue arg)
	{
		try
		{
			String toPrint = "";
			for(int i = 0; i < arg.narg(); i++ )
			{
				if(i != 0) toPrint += "\t";
				toPrint += arg.arg(i + 1);
			}
			String scriptName = globals.get("debug").get("getinfo").call("1").get("source").toString();
			Log.message("[Script " + scriptName + "] " + toPrint);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return LuaValue.NIL;
	}

}
