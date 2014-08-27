package org.jge.components.hud.events;

import org.jge.components.hud.HUDWidget;

public abstract class MouseEvent extends WidgetEvent
{

	public int x;
	public int y;

	public MouseEvent(HUDWidget source, int x, int y)
	{
		super(source);
		this.x = x;
		this.y = y;
	}

}
