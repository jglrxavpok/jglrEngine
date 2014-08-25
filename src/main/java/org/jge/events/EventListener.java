package org.jge.events;

public class EventListener
{
	private Object listener;
	private String methodName;
	private boolean enabled = true;

	public EventListener(Object object, String methodName)
	{
		this.listener = object;
		this.methodName = methodName; 
	}

	public Object getListener()
	{
		return listener;
	}

	public String getMethodName()
	{
		return methodName;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void enable()
	{
		this.enabled = true;
	}
	
	public void disable()
	{
		this.enabled = false;
	}
}
