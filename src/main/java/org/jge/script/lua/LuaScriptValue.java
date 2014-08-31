package org.jge.script.lua;

import org.jge.script.ScriptValue;

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
public class LuaScriptValue implements ScriptValue
{

	private LuaValue luaVal;

	public LuaScriptValue(LuaValue luaVal)
	{
		this.luaVal = luaVal;
	}

	@Override
	public int asInt()
	{
		return luaVal.checkint();
	}

	@Override
	public double asDouble()
	{
		return luaVal.checkdouble();
	}

	@Override
	public float asFloat()
	{
		return (float)asDouble();
	}

	@Override
	public boolean asBoolean()
	{
		return luaVal.checkboolean();
	}

	@Override
	public String toString()
	{
		return luaVal.toString();
	}

	@Override
	public int length()
	{
		return luaVal.length();
	}

	@Override
	public ScriptValue getComponent(int index)
	{
		return new LuaScriptValue(luaVal.get(index));
	}

	@Override
	public ScriptValue getComponent(String name)
	{
		return new LuaScriptValue(luaVal.get(name));
	}

	@Override
	public ScriptValue invoke(ScriptValue... args)
	{
		if(luaVal.isfunction())
		{
			LuaValue[] luaArgs = new LuaValue[args.length];
			for(int i = 0; i < args.length; i++ )
			{
				ScriptValue arg = args[i];
				if(arg.getType() == ScriptValue.TYPE_STRING)
				{
					luaArgs[i] = LuaValue.valueOf(arg.toString());
				}
				else if(arg.getType() == ScriptValue.TYPE_INTEGER)
				{
					luaArgs[i] = LuaValue.valueOf(arg.asInt());
				}
				else if(arg.getType() == ScriptValue.TYPE_DOUBLE)
				{
					luaArgs[i] = LuaValue.valueOf(arg.asDouble());
				}
				else if(arg.getType() == ScriptValue.TYPE_BOOLEAN)
				{
					luaArgs[i] = LuaValue.valueOf(arg.asBoolean());
				}
				// else if(arg.getType() == ScriptValue.TYPE_TABLE)
				// {
				// luaArgs[i] = LuaValue.valueOf(arg.asTable()); TODO: implement
				// }
				// else if(arg.getType() == ScriptValue.TYPE_byte[])
				// {
				// luaArgs[i] = LuaValue.valueOf((byte[])arg);
				// }
				else
					luaArgs[i] = LuaValue.NIL;
			}
			return new LuaScriptVarargs(luaVal.checkfunction().invoke(luaArgs));
		}
		else
			return this;
	}

	@Override
	public String getType()
	{
		if(luaVal.isboolean()) return ScriptValue.TYPE_BOOLEAN;
		if(luaVal.isstring()) return ScriptValue.TYPE_STRING;
		if(luaVal.isint()) return ScriptValue.TYPE_INTEGER;
		if(luaVal.isnumber()) return ScriptValue.TYPE_DOUBLE;
		return ScriptValue.TYPE_UNKNOWN;
	}

}
