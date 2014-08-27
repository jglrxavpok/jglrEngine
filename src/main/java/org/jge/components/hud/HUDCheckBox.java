package org.jge.components.hud;

import org.jge.components.Camera;
import org.jge.components.hud.events.ClickEvent;
import org.jge.components.hud.events.SelectionStateChanged;
import org.jge.events.Event.EventSubscribe;
import org.jge.render.RenderEngine;
import org.jge.render.Sprite;
import org.jge.render.shaders.Shader;

public class HUDCheckBox extends HUDWidget
{

	private boolean selected;

	public HUDCheckBox(double w, double h)
	{
		this(w, h, false);
	}

	public HUDCheckBox(double w, double h, boolean selected)
	{
		super(w, h);
		this.selected = selected;
		this.registerListener(this);
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void render(Shader shader, Camera camera, double delta, RenderEngine engine)
	{
		double startX = isMouseOn ? 64 : 0;
		double startY = 96;
		drawBox(shader, camera, engine, startX, startY);

		if(selected)
		{
			Sprite sign = new Sprite(widgetTexture, isMouseOn ? 24 : 0, 144, 24, 24);
			sign.setX(4).setY(4);
			sign.setWidth(getWidth() - 8).setHeight(getHeight() - 8);
			sign.render(shader, getTransform(), camera, delta, engine);
		}
	}

	@EventSubscribe
	private void onClick(ClickEvent event)
	{
		SelectionStateChanged e = new SelectionStateChanged(this, selected, !selected);
		fireEvent(e);
		selected = e.newState;
	}
}
