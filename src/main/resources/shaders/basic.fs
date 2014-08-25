#version 120

varying vec2 texCoord0;
uniform sampler2D diffuse;
uniform vec4 R_remplacementColor;
uniform bool R_hasRemplacementColor;


void main()
{
	vec4 color = texture2D(diffuse, texCoord0);
	if(color.a != 0 && R_hasRemplacementColor)
	{
		color = R_remplacementColor;
	}

	gl_FragColor = color;
}