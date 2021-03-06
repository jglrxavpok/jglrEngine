package org.jge;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class VirtualResourceLoader extends ResourceLoader
{

	private static VirtualResourceLoader	  quickLoader;
	private HashMap<String, AbstractResource> resources = new HashMap<String, AbstractResource>();

	@Override
	public AbstractResource getResource(ResourceLocation location) throws Exception
	{
		if(!resources.containsKey(location.getFullPath()))
		{
			throw new FileNotFoundException("Virtual resource /" + location.getFullPath() + " not found.");
		}
		return resources.get(location.getFullPath());
	}

	public boolean isLoaded(ResourceLocation location)
	{
		return resources.containsKey(location.getFullPath());
	}

	@Override
	public List<AbstractResource> getAllResources(ResourceLocation location) throws Exception
	{
		return Arrays.asList(getResource(location));
	}

	@Override
	public boolean doesResourceExists(ResourceLocation location)
	{
		try
		{
			if(resources.containsKey(location.getFullPath())) return true;
		}
		catch(Exception e)
		{
			return false;
		}
		return false;
	}

	public void addResource(String type, byte[] bytes)
	{
		resources.put(type, new VirtualResource(type, bytes, this));
	}

	public static AbstractResource quickCreateResource(byte[] bytes) throws Exception
	{
		if(quickLoader == null) quickLoader = new VirtualResourceLoader();
		quickLoader.addResource("object_" + bytes.hashCode(), bytes);
		return quickLoader.getResource(new ResourceLocation("object_" + bytes.hashCode()));
	}

}
