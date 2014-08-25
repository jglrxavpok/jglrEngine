package org.jge;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import org.jge.util.Lists;

public class VirtualResourceLoader extends ResourceLoader
{

    private HashMap<String, AbstractResource> resources = new HashMap<String, AbstractResource>();
    
    @Override
    public AbstractResource getResource(ResourceLocation location) throws Exception
    {
        if(!resources.containsKey(location.getFullPath()))
        {
        	throw new FileNotFoundException("Virtual resource /"+location.getFullPath()+" not found.");
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
        return Lists.asList(getResource(location));
    }

	@Override
	public boolean doesResourceExists(ResourceLocation location)
	{
		try
		{
			if(resources.containsKey(location.getFullPath()))
				return true;
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

}
