package org.jge.components.hud.events;

import org.jge.components.hud.HUDWidget;

public class ClickEvent extends MouseEvent
{

	public ClickEvent(HUDWidget source, int x, int y)
	{
		super(source, x, y);
	}

	@Override
	public boolean isCancellable()
	{
		return false;
	}

}
