package org.jge.components.hud.events;

import org.jge.components.hud.HUDCheckBox;

public class SelectionStateChanged extends WidgetEvent
{

	public boolean oldState;
	public boolean newState;

	public SelectionStateChanged(HUDCheckBox checkBox, boolean oldState, boolean newState)
	{
		super(checkBox);
		this.oldState = oldState;
		this.newState = newState;
	}

	@Override
	public boolean isCancellable()
	{
		return false;
	}

}
