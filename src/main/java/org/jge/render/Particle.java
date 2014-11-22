package org.jge.render;

import org.jge.maths.Transform;
import org.jge.maths.Vector3;

public class Particle
{

	private Vector3   pos;
	private Vector3   velocity;
	private int	   life;
	private boolean   dead;
	private Vector3   gravityForce;
	private Sprite	sprite;
	private Transform trans;
	private int	   ticksExisted;
	private int	   maxLife;

	public Particle(Vector3 pos, Vector3 velocity)
	{
		this(pos, velocity, 60);
	}

	public Particle(Vector3 pos, Vector3 velocity, int life)
	{
		this(pos, velocity, life, Vector3.NULL);
	}

	public Particle(Vector3 pos, Vector3 velocity, int life, Vector3 gravity)
	{
		this(pos, velocity, life, gravity, new Sprite(ParticleSystem.particleMaterial.getTexture("diffuse"), 0, 0, 16, 16));
	}

	public Particle(Vector3 pos, Vector3 velocity, int life, Vector3 gravity, Sprite sprite)
	{
		this.pos = pos;
		this.velocity = velocity;
		this.life = life;
		this.maxLife = life;
		this.gravityForce = gravity;
		this.sprite = sprite;
		this.trans = new Transform();
	}

	public void update(double delta)
	{
		life-- ;
		ticksExisted++ ;
		velocity = velocity.add(gravityForce.mul((float)delta));
		pos = pos.add(velocity.mul((float)delta));
		if(life <= 0) dead = true;
	}

	public int getMaxLife()
	{
		return maxLife;
	}

	public int getTicksExisted()
	{
		return ticksExisted;
	}

	public Vector3 getPos()
	{
		return pos;
	}

	public void setPos(Vector3 pos)
	{
		this.pos = pos;
	}

	public Vector3 getVelocity()
	{
		return velocity;
	}

	public void setVelocity(Vector3 velocity)
	{
		this.velocity = velocity;
	}

	public boolean isDead()
	{
		return dead;
	}

	public Vector3 getGravityForce()
	{
		return gravityForce;
	}

	public void setGravityForce(Vector3 gravityForce)
	{
		this.gravityForce = gravityForce;
	}

	public float getSize()
	{
		return 2f;
	}

	public Sprite getSprite()
	{
		return sprite;
	}

	public Transform getTransform()
	{
		return trans;
	}
}
