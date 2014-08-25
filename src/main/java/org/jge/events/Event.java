package org.jge.events;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class Event
{

	@Retention(RetentionPolicy.RUNTIME) 
	public @interface EventSubscribe {}
	
	private boolean cancelled;
	
	public Event()
	{
	}
	
	public Event cancel()
	{
		cancelled = true;
		return this;
	}
	
	public Event setCancelled(boolean c)
	{
		this.cancelled = c;
		return this;
	}

	public abstract boolean isCancellable();
	
	public boolean isCancelled()
	{
		return cancelled;
	}
}
