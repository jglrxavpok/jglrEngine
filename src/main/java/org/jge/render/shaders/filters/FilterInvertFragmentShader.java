package org.jge.render.shaders.filters;

import org.jge.maths.Quaternion;
import org.jge.maths.Vector2;
import org.jge.render.shaders.JGEFragmentShader;
import org.jglrxavpok.jlsl.glsl.Sampler2D;
import org.jglrxavpok.jlsl.glsl.GLSL.Varying;

public class FilterInvertFragmentShader extends JGEFragmentShader
{

	@Varying
	public Vector2 texCoord0;
	
	public Sampler2D R_filterTexture;

	@SuppressWarnings("deprecation")
	public void main()
	{
		Vector2 texCoords = texCoord0;
		gl_FragColor = new Quaternion(1,1,1,1).sub(texture(R_filterTexture, new Vector2(texCoords.x, texCoords.y)));
	}


}
