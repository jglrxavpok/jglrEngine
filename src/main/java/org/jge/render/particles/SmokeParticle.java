package org.jge.render.particles;

import org.jge.maths.Maths;
import org.jge.maths.Vector3;
import org.jge.render.Particle;
import org.jge.render.ParticleSystem;
import org.jge.render.Sprite;

public class SmokeParticle extends Particle
{

	private Sprite[] spritesArray;

	public SmokeParticle(Vector3 pos, Vector3 velocity, boolean black)
	{
		this(pos, velocity, 60, black);
	}
	
	public SmokeParticle(Vector3 pos, Vector3 velocity, int life, boolean black)
	{
		this(pos, velocity, life, Vector3.NULL, black);
	}
	
	public SmokeParticle(Vector3 pos, Vector3 velocity, int life, Vector3 gravity, boolean black)
	{
		super(pos, velocity, life, gravity, new Sprite(ParticleSystem.particleMaterial.getTexture("diffuse"), 0,0,16,16));
		
		int spritesNbr = 10;
		spritesArray = new Sprite[spritesNbr];
		for(int i = 0 ; i<spritesNbr; i++)
		{
			spritesArray[i] = new Sprite(ParticleSystem.particleMaterial.getTexture("diffuse"), 0+i*16, black ? 32 : 16 ,16,16);
		}
	}
	
	public Sprite getSprite()
	{
		double time = (double)getTicksExisted()/(double)getMaxLife();
		return spritesArray[(int)(Maths.floor(time* spritesArray.length-1))];
	}
	
}
