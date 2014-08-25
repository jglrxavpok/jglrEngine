#version 120

varying vec2 texCoord0;
uniform sampler2D diffuse;
uniform int R_lightNumber;

void main()
{
	vec4 color = texture2D(diffuse, texCoord0);
	vec3 rgbPart = color.rgb / R_lightNumber; 
	gl_FragColor = vec4(rgbPart, color.w);
}