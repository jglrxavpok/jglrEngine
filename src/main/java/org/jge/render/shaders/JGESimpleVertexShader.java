package org.jge.render.shaders;

import org.jge.maths.Matrix4;
import org.jge.maths.Quaternion;
import org.jge.maths.Vector2;
import org.jge.maths.Vector3;
import org.jglrxavpok.jlsl.glsl.GLSL.Attribute;
import org.jglrxavpok.jlsl.glsl.GLSL.Varying;

public class JGESimpleVertexShader extends JGEVertexShader
{

	@Attribute
	public Vector3 position;
	
	@Attribute
	public Vector2 texCoord;

	@Varying
	public Vector2 texCoord0;

	public Matrix4 T_worldTransform;
	public Matrix4 T_projectedView;

	@SuppressWarnings("deprecation")
	public void main()
	{
		gl_Position = T_projectedView.mul(T_worldTransform).transform(new Quaternion(position,1.0));
	    texCoord0 = texCoord;
	    vertexMain();
	}
	
	public void vertexMain()
	{
		
	}

}
