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
				addParticle(new SmokeParticle(pos, new Vector3(Maths.rand() * 3.0 - 1.5, Maths.rand() * 2.0 - 1.0, Maths.rand() * 3.0 - 1.5), 120, new Vector3(0, 2.5, 0), false));
			}
		}
		super.update(delta);
	}
}
