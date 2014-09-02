package org.jge.render.shaders;

import org.jge.maths.Quaternion;
import org.jge.maths.Vector2;

import org.jglrxavpok.jlsl.ObfuscationFilter.NonObfuscable;
import org.jglrxavpok.jlsl.glsl.Sampler2D;
import org.jglrxavpok.jlsl.glsl.ShaderBase;

public abstract class JGEFragmentShader extends ShaderBase
{
	@Deprecated
	public Quaternion gl_FragColor;

	@Deprecated
	public Quaternion gl_FragCoord;

	@NonObfuscable
	public Quaternion texture(Sampler2D texture, Vector2 coords)
	{
		return new Quaternion(0, 0, 0, 0);
	}

	@Deprecated
	@NonObfuscable
	public Quaternion texture2D(Sampler2D texture, Vector2 coords)
	{
		return new Quaternion(0, 0, 0, 0);
	}

}
