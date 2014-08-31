package org.jge.script.lua;

import java.io.UnsupportedEncodingException;

import org.jge.AbstractResource;
import org.jge.script.Script;
import org.jge.script.ScriptValue;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

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
public class LuaScript extends Script
{
	private Globals globals;

	public LuaScript(AbstractResource scriptRes) throws UnsupportedEncodingException
	{
		super(scriptRes);
	}

	public LuaScript(AbstractResource newRes, Globals globals) throws UnsupportedEncodingException
	{
		super();
		this.globals = globals;
		init(newRes);
	}

	@Override
	protected void compile()
	{
		if(globals == null) globals = JsePlatform.debugGlobals();
		globals.set("require", new RequireFunction(scriptResource, globals));
		globals.set("print", new PrintFunction(globals));
		globals.load(new EngineLib());
		globals.load(scriptSource, scriptResource.getResourceLocation().getName()).call();
	}

	@Override
	public ScriptValue run()
	{
		return new LuaScriptValue(globals.load(scriptSource).call());
	}

	@Override
	public ScriptValue run(String method, Object... args)
	{
		LuaValue[] luaArgs = new LuaValue[args.length];
		for(int i = 0; i < args.length; i++ )
		{
			Object arg = args[i];
			if(arg instanceof String)
			{
				luaArgs[i] = LuaValue.valueOf((String)arg);
			}
			else if(arg instanceof Integer)
			{
				luaArgs[i] = LuaValue.valueOf((Integer)arg);
			}
			else if(arg instanceof Double)
			{
				luaArgs[i] = LuaValue.valueOf((Double)arg);
			}
			else if(arg instanceof Boolean)
			{
				luaArgs[i] = LuaValue.valueOf((Boolean)arg);
			}
			else if(arg instanceof byte[])
			{
				luaArgs[i] = LuaValue.valueOf((byte[])arg);
			}
			else
				luaArgs[i] = LuaValue.NIL;
		}
		return new LuaScriptVarargs(globals.get(method).invoke(LuaValue.varargsOf(luaArgs)));
	}

}
