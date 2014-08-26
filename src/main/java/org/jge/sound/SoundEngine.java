package org.jge.sound;

import org.jge.AbstractResource;
import org.jge.EngineException;
import org.jge.RuntimeEngineException;
import org.jge.maths.Transform;
import org.jge.maths.Vector3;
import org.jge.util.Buffers;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

public class SoundEngine
{

	private int maxSourcesPerSound;
	private Transform playerTrans;

	public SoundEngine()
	{
		maxSourcesPerSound = 16;
		
		try
		{
			AL.create();
		}
		catch(LWJGLException e)
		{
			e.printStackTrace();
		}
	}

	public void init()
	{
	}

	public void update(double delta)
	{
		if(getPlayerTransform() != null)
    		if(getPlayerTransform().hasChanged())
    		{
    			AL10.alListener(AL10.AL_POSITION, Buffers.createFlippedBuffer(getPlayerTransform().getTransformedPos()));
    			AL10.alListener(AL10.AL_VELOCITY, Buffers.createFlippedBuffer(Vector3.NULL));
    			AL10.alListener(AL10.AL_ORIENTATION, Buffers.createFlippedBuffer(getPlayerTransform().getTransformedRotation().getForward().negative().normalize(), 
    					getPlayerTransform().getTransformedRotation().getUp().normalize()));
    		}
	}
	
	public Transform getPlayerTransform()
	{
		return playerTrans;
	}
	
	public SoundEngine setPlayerTransform(Transform trans)
	{
		playerTrans = trans;
		return this;
	}

	public SoundResource loadSound(AbstractResource res)
	{
		String ext = res.getResourceLocation().getExtension().toUpperCase();
		SoundFormats format = SoundFormats.valueOf(ext);
		if(format == null)
			throw new RuntimeEngineException("Unsupported sound format: " + ext);
		try
		{
			return format.getLoader().loadResource(res.getData());
		}
		catch(EngineException e)
		{
			throw new RuntimeEngineException("Error while loading sound "
					+ res.getResourceLocation().getFullPath(), e);
		}
	}

	public String getOpenALVendor()
	{
		return AL10.alGetString(AL10.AL_VENDOR);
	}

	public String getOpenALVersion()
	{
		return AL10.alGetString(AL10.AL_VERSION);
	}

	public static String getALErrorString(int err)
	{
		switch(err)
		{
			case AL10.AL_NO_ERROR:
				return "AL_NO_ERROR";
			case AL10.AL_INVALID_NAME:
				return "AL_INVALID_NAME";
			case AL10.AL_INVALID_ENUM:
				return "AL_INVALID_ENUM";
			case AL10.AL_INVALID_VALUE:
				return "AL_INVALID_VALUE";
			case AL10.AL_INVALID_OPERATION:
				return "AL_INVALID_OPERATION";
			case AL10.AL_OUT_OF_MEMORY:
				return "AL_OUT_OF_MEMORY";
			default:
				return "No such error code";
		}
	}

	public int getMaxSourcesPerSound()
	{
		return maxSourcesPerSound;
	}
	
	public SoundEngine setMaxSourcesPerSound(int max)
	{
		this.maxSourcesPerSound = max;
		return this;
	}
}
