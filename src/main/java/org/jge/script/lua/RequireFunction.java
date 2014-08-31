package org.jge.script.lua;

import java.io.UnsupportedEncodingException;

import org.jge.AbstractResource;
import org.jge.CoreEngine;
import org.jge.ResourceLocation;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;

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
public class RequireFunction extends LuaFunction
{

	private AbstractResource scriptResource;
	private Globals		  globals;

	public RequireFunction(AbstractResource scriptResource, Globals globals)
	{
		this.scriptResource = scriptResource;
		this.globals = globals;
	}

	public LuaValue call(LuaValue arg)
	{
		String scriptName = arg.toString();
		try
		{
			if(scriptName.equals("__jglrEngine__"))
			{
				new LuaScript(CoreEngine.getCurrent().getClasspathResourceLoader().getResource(new ResourceLocation("text", "engine.lua")), globals);
			}
			else
			{
				AbstractResource newRes = scriptResource.getLoader().getResource(scriptResource.getResourceLocation().getDirectParent().getChild(scriptName));
				new LuaScript(newRes, globals);
			}
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return LuaValue.NIL;
	}
}
