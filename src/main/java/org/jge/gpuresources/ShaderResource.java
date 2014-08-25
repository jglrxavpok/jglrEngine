package org.jge.gpuresources;

import org.lwjgl.opengl.GL20;

public class ShaderResource
{

	private int program;
    private int referenceCounter;
	private String name;
    
    public ShaderResource()
    {
    	this.program = GL20.glCreateProgram();
    	referenceCounter = 1;
    }
    
    public boolean decreaseCounter()
    {
    	referenceCounter--;
    	return referenceCounter <= 0;
    }
    
    public void increaseCounter()
    {
    	referenceCounter++;
    }
    
    @Override
    protected void finalize()
    {
    	dispose();
    }
    
    public void dispose()
    {
    	GL20.glDeleteProgram(program);
    }

	public int getProgram()
	{
		return program;
	}

	public ShaderResource bindName(String path)
	{
		this.name = path;
		return this;
	}
	
	public String getBindName()
	{
		return name;
	}
}
