package org.jge.render.shaders.filters;

import org.jge.maths.Quaternion;
import org.jge.maths.Vector2;
import org.jge.maths.Vector3;
import org.jge.render.shaders.JGEFragmentShader;

import org.jglrxavpok.jlsl.glsl.GLSL.Varying;
import org.jglrxavpok.jlsl.glsl.Sampler2D;

public class FilterInvertFragmentShader extends JGEFragmentShader
{

	@Varying
	public Vector2   texCoord0;

	public Sampler2D R_filterTexture;

	@SuppressWarnings("deprecation")
	public void main()
	{
		Vector2 texCoords = texCoord0;
		Quaternion color = texture(R_filterTexture, texCoords);
		gl_FragColor = new Quaternion(new Vector3(1, 1, 1).sub(color.xyz()), color.getW());
	}

}
