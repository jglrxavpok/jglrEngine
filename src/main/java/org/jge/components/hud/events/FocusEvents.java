package org.jge.components.hud.events;

import org.jge.components.hud.HUDWidget;

public class FocusEvents
{
	public static class FocusGainedEvent extends MouseEvent
	{

		public FocusGainedEvent(HUDWidget source, int x, int y)
		{
			super(source, x, y);
		}

		@Override
		public boolean isCancellable()
		{
			return true;
		}

	}

	public static class FocusLostEvent extends MouseEvent
	{

		public FocusLostEvent(HUDWidget source, int x, int y)
		{
			super(source, x, y);
		}

		@Override
		public boolean isCancellable()
		{
			return true;
		}

	}
}
