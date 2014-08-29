package org.jge.script;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.jge.AbstractResource;
import org.jge.util.BinaryUtils;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
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
public class Script
{

	private String   scriptSource;
	private LuaValue compiledScript;
	private Globals  globals;

	public Script(AbstractResource scriptRes) throws UnsupportedEncodingException
	{
		scriptSource = BinaryUtils.toString(scriptRes.getData());
		compile(scriptSource);
	}

	private void compile(String scriptContent2)
	{
		globals = JsePlatform.standardGlobals();
		compiledScript = globals.load(scriptSource, "testLua").call();
	}

	public Varargs run(String method, Object... args) throws IOException
	{
		return globals.get(method).call((String)args[0]);
	}

	public LuaValue run()
	{
		return globals.load(scriptSource).call();
	}
}
