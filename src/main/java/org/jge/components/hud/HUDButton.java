package org.jge.components.hud;

import org.jge.components.Camera;
import org.jge.render.RenderEngine;
import org.jge.render.fonts.Font;
import org.jge.render.shaders.Shader;

public class HUDButton extends HUDWidget
{

	private String text;
	private Font   font;

	public HUDButton(float w, float h, String text, Font font)
	{
		super(w, h);
		this.text = text;
		this.font = font;
	}

	public void render(Shader shader, Camera camera, double delta, RenderEngine engine)
	{
		float startX = isFocused ? 0 : isMouseOn ? 64 : 0;
		float startY = isFocused ? 48 : 0;
		drawBox(shader, camera, engine, startX, startY);

		font.drawString(shader, text, getTransform(), 0xFF000000, getWidth() / 2 - font.getTextLength(text) / 2 + 1, getHeight() / 2 - font.getCharHeight('H') / 2 - 1, camera, engine);
		font.drawString(shader, text, getTransform(), !(isMouseOn || isFocused) ? 0xFFFFFFFF : 0xFFFFFF00, getWidth() / 2 - font.getTextLength(text) / 2, getHeight() / 2 - font.getCharHeight('H') / 2, camera, engine);

	}

	public String getText()
	{
		return text;
	}

	public HUDButton setText(String newText)
	{
		this.text = newText;
		return this;
	}

}
