#version 120

varying vec2 texCoord0;
uniform sampler2D R_filterTexture;
uniform float E_tick;
uniform float R_SizeOf_diffuse_X;
uniform float R_SizeOf_diffuse_Y;
uniform float S_direction = -1;
uniform float S_bandHeight = 25;
uniform bool S_vertical = false;
uniform int S_number = 10;
uniform vec3 S_bandColor = vec3(0,0,0);

void main()
{
	float size = 1.0/((S_bandHeight*S_number)/R_SizeOf_diffuse_Y);
	vec4 color = texture2D(R_filterTexture, texCoord0);
	float stepWidth = R_SizeOf_diffuse_X/S_number;
	float stepHeight = R_SizeOf_diffuse_Y/S_number;
	for(int i = 0;i<S_number;i++)
	{
		float distance = 0;
		if(S_vertical)
		{
			distance = abs(texCoord0.x - (mod(S_direction*E_tick+stepWidth*i, R_SizeOf_diffuse_X)) / R_SizeOf_diffuse_X);
		}
		else
		{
			distance = abs(texCoord0.y - (mod(S_direction*E_tick+stepHeight*i, R_SizeOf_diffuse_Y)) / R_SizeOf_diffuse_Y);
		}
		if(distance < 0.000001)
			distance = 0.000001;
		if(1.0-distance < distance)
		{
			distance = 1.0-distance;
		}
		float ratio = (distance*S_number*size);
		if(ratio < 0.4)
			ratio = 0.4;
		if(ratio > 1.0)
			ratio = 1.0;
		color = mix(color, vec4(S_bandColor*2,1), 1.0-ratio);
	}
	gl_FragColor = color;
}
