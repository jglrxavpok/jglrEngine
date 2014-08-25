package org.jge.components.hud.events;

import org.jge.components.hud.HUDWidget;
import org.jge.events.Event;

public abstract class WidgetEvent extends Event
{

	private HUDWidget source;
	
	public WidgetEvent(HUDWidget source)
	{
		this.source = source;
	}
	
	public HUDWidget getSource()
	{
		return source;
	}
	

}
