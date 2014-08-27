package org.jge.sound;

import java.util.HashMap;

import org.jge.AbstractResource;
import org.jge.CoreEngine;
import org.jge.Disposable;
import org.jge.JGEngine;
import org.jge.maths.Maths;
import org.jge.maths.Vector3;
import org.jge.util.Buffers;
import org.jge.util.Log;

import org.lwjgl.openal.AL10;

public class Sound implements Disposable
{

	private static HashMap<AbstractResource, SoundResource> loadedResources = new HashMap<AbstractResource, SoundResource>();
	private SoundResource								   data;
	private AbstractResource								res;
	private int[]										   sources;
	private int											 sourceIndex	 = 0;

	public Sound(AbstractResource res)
	{
		this(res, CoreEngine.getCurrent().getSoundEngine().getMaxSourcesPerSound());
	}

	public Sound(AbstractResource res, int buffers)
	{
		sources = new int[Maths.min(buffers, CoreEngine.getCurrent().getSoundEngine().getMaxSourcesPerSound())];
		this.res = res;
		SoundResource existingResource = loadedResources.get(res);

		if(existingResource != null)
		{
			JGEngine.getDisposer().add(this);
			data = existingResource;
			data.increaseCounter();
		}
		else
		{
			data = CoreEngine.getCurrent().getSoundEngine().loadSound(res);
			loadedResources.put(res, data);
		}
	}

	public void dispose()
	{
		if(data.decreaseCounter())
		{
			data.dispose();
			if(res != null) loadedResources.remove(res);
		}
	}

	public int play()
	{
		nextSoundIndex();
		printIfError();
		AL10.alSourcePlay(sources[sourceIndex]);
		return sources[sourceIndex];
	}

	int nextSoundIndex()
	{
		sourceIndex++ ;
		if(sourceIndex >= sources.length)
		{
			sourceIndex = 0;
		}
		if(sources[sourceIndex] == 0) sources[sourceIndex] = data.createSourceID();
		return sources[sourceIndex];
	}

	public Sound rewind(int source)
	{
		AL10.alSourceRewind(source);
		printIfError();
		return this;
	}

	public Sound rewind()
	{
		return rewind(sources[sourceIndex]);
	}

	public Sound resume(int source)
	{
		AL10.alSourcePlay(source);
		printIfError();
		return this;
	}

	public Sound resume()
	{
		return resume(sources[sourceIndex]);
	}

	public Sound pause(int source)
	{
		if(isPlaying(source)) AL10.alSourcePause(source);
		printIfError();
		return this;
	}

	public Sound pause()
	{
		return pause(sources[sourceIndex]);
	}

	public Sound stop(int source)
	{
		if(isPlaying(source)) AL10.alSourceStop(source);
		printIfError();
		return this;
	}

	public Sound stop()
	{
		return stop(sources[sourceIndex]);
	}

	private static void printIfError()
	{
		int error = AL10.alGetError();
		if(error != AL10.AL_NO_ERROR)
		{
			Log.error(SoundEngine.getALErrorString(error));
		}
	}

	public Sound setSourcePosition(int id, Vector3 pos)
	{
		AL10.alSource(id, AL10.AL_POSITION, Buffers.createFlippedBuffer(pos));
		return this;
	}

	public Sound setSourceVelocity(int id, Vector3 vel)
	{
		AL10.alSource(id, AL10.AL_VELOCITY, Buffers.createFlippedBuffer(vel));
		return this;
	}

	public boolean isPlaying()
	{
		return isPlaying(sources[sourceIndex]);
	}

	public boolean isPlaying(int source)
	{
		return AL10.alGetSourcei(source, AL10.AL_PLAYING) != 0;
	}

	public Sound setLooping(boolean loop)
	{
		for(int source : sources)
		{
			if(source != 0) AL10.alSourcei(source, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
		}
		return this;
	}

	public Sound setGain(float gain)
	{
		for(int source : sources)
		{
			if(source != 0) AL10.alSourcef(source, AL10.AL_GAIN, gain);
		}
		return this;
	}
}
