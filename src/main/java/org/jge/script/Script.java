package org.jge.script;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.jge.AbstractResource;
import org.jge.util.BinaryUtils;

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
public abstract class Script
{

	protected String		   scriptSource;
	protected AbstractResource scriptResource;
	private String			 filename;

	public Script(AbstractResource scriptRes) throws UnsupportedEncodingException
	{
		init(scriptRes);
	}

	protected void init(AbstractResource scriptRes) throws UnsupportedEncodingException
	{
		scriptSource = BinaryUtils.toString(scriptRes.getData());
		this.scriptResource = scriptRes;
		this.filename = scriptResource.getResourceLocation().getName();
		compile();
	}

	public Script()
	{
	}

	public String getScriptFileName()
	{
		return filename;
	}

	protected abstract void compile();

	public abstract ScriptValue run(String method, Object... args) throws IOException;

	public abstract ScriptValue run();
}
