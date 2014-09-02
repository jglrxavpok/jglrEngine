package org.jge.render;

import org.jge.maths.Maths;
import org.jge.maths.Vector3;
import org.jge.render.particles.SmokeParticle;

public class ParticleGenerator extends ParticleSystem
{

	private long spawnDelay;
	private long timer;
	private int  spawnNumber;

	public ParticleGenerator(long spawnDelay)
	{
		this(spawnDelay, 2);
	}

	public ParticleGenerator(long spawnDelay, int spawnNumber)
	{
		this(2000, spawnDelay, spawnNumber);
	}

	public ParticleGenerator(int max, long spawnDelay, int spawnNumber)
	{
		super(max);
		this.spawnNumber = spawnNumber;
		this.spawnDelay = spawnDelay;
	}

	public void update(double delta)
	{
		if((timer += delta * 1000) >= spawnDelay)
		{
			timer = 0;
			for(int i = 0; i < spawnNumber; i++ )
			{
				Vector3 pos = getTransform().getTransformedPos();
				addParticle(new SmokeParticle(pos, Vector3.get(Maths.randf() * 3.0f - 1.5f, Maths.randf() * 2.0f - 1.0f, Maths.randf() * 3.0f - 1.5f), 120, Vector3.get(0, 2.5f, 0), false));
			}
		}
		super.update(delta);
	}
}
