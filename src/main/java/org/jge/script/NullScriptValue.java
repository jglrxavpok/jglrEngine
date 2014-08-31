package org.jge.script;

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
public class NullScriptValue extends ScriptValue
{

	@Override
	public ScriptValue getComponent(int index)
	{
		throw new NullPointerException("Attempted to access index " + index + " of a null value");
	}

	@Override
	public ScriptValue getComponent(String name)
	{
		throw new NullPointerException("Attempted to access component " + name + " of a null value");
	}

	@Override
	public int length()
	{
		return 0;
	}

	@Override
	public String toString()
	{
		return "null";
	}

	@Override
	public int asInt()
	{
		return 0;
	}

	@Override
	public float asFloat()
	{
		return 0;
	}

	@Override
	public double asDouble()
	{
		return 0;
	}

	@Override
	public boolean asBoolean()
	{
		return false;
	}

	@Override
	public ScriptValue invoke(ScriptValue... values)
	{
		return null;
	}

	@Override
	public String getType()
	{
		return null;
	}

}
