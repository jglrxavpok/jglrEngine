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
public interface ScriptValue
{

	public static final ScriptValue NULL		 = new NullScriptValue();
	public static final String	  TYPE_STRING  = "TString";
	public static final String	  TYPE_INTEGER = "TInt";
	public static final String	  TYPE_DOUBLE  = "TDouble";
	public static final String	  TYPE_BOOLEAN = "TBoolean";
	public static final String	  TYPE_UNKNOWN = "TUnknown";
	public static final String	  TYPE_TABLE   = "TTable";

	public ScriptValue getComponent(int index);

	public ScriptValue getComponent(String name);

	public int length();

	public String toString();

	public int asInt();

	public float asFloat();

	public double asDouble();

	public boolean asBoolean();

	public ScriptValue invoke(ScriptValue... values);

	public abstract String getType();
}
