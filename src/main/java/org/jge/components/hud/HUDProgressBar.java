package org.jge.components.hud;

import static org.lwjgl.opengl.GL11.*;

import org.jge.components.Camera;
import org.jge.maths.Maths;
import org.jge.render.RenderEngine;
import org.jge.render.fonts.Font;
import org.jge.render.shaders.Shader;

public class HUDProgressBar extends HUDWidget
{

	private Font	font;
	private String  text;
	private boolean showText;
	private double  progress;
	private boolean drawProgressBackground;
	private boolean undertermined;
	private int	 underterminedCounter;
	private boolean showProgressString;

	public HUDProgressBar(double w, double h, Font font)
	{
		super(w, h);
		this.font = font;
		progress = 0.5;
		text = "Testing stuff";
		showText = true;
		showProgressString = true;
	}

	public boolean isShowingProgressString()
	{
		return showProgressString;
	}

	public HUDProgressBar setShowProgressText(boolean showProgressString)
	{
		this.showProgressString = showProgressString;
		return this;
	}

	public boolean isUndertermined()
	{
		return undertermined;
	}

	public HUDProgressBar setUndertermined(boolean undertermined)
	{
		this.undertermined = undertermined;
		return this;
	}

	public boolean drawsProgressBackground()
	{
		return drawProgressBackground;
	}

	public HUDProgressBar setProgressBackgroundDrawing(boolean draw)
	{
		this.drawProgressBackground = draw;
		return this;
	}

	public String getText()
	{
		return text;
	}

	public double getProgress()
	{
		return progress;
	}

	public HUDProgressBar setProgress(double progress)
	{
		this.progress = progress;
		return this;
	}

	public HUDProgressBar setText(String text)
	{
		this.text = text;
		return this;
	}

	public boolean showsText()
	{
		return showText;
	}

	public HUDProgressBar showText(boolean showText)
	{
		this.showText = showText;
		return this;
	}

	public void render(Shader shader, Camera camera, double delta, RenderEngine engine)
	{
		double startY = 168;
		drawBox(shader, camera, engine, 0, startY);
		progress += 0.001;
		if(progress > 1.0) progress = 0.0;
		if(progress < 0.0) progress = 0.0;
		if(drawProgressBackground)
		{
			drawBox(shader, camera, engine, 0, startY + 32, getWidth(), getHeight());
		}
		if(!undertermined)
		{
			drawBox(shader, camera, engine, 64, startY, progress * getWidth(), getHeight());
			underterminedCounter = 0;
		}
		else
		{
			underterminedCounter++ ;
			double p = (double)(underterminedCounter) / getWidth();
			if(p * getWidth() + 48 > getWidth())
			{
				underterminedCounter = 0;
				p = 0;
			}
			getTransform().translate(p * getWidth(), 0, 0);
			drawBox(shader, camera, engine, 64, startY, 48, getHeight());
			getTransform().translate(-p * getWidth(), 0, 0);
		}

		String toDraw = "";
		if(text != null && showText)
		{
			toDraw = text;
		}
		if(showProgressString)
		{
			if(toDraw.length() > 0)
				toDraw += " - " + Maths.roundToNthDecimal(progress * 100, 1);
			else
				toDraw = "" + Maths.roundToNthDecimal(progress * 100, 1);
		}
		engine.enableGLCap(GL_COLOR_LOGIC_OP);
		glLogicOp(GL_XOR);
		font.drawString(shader, toDraw, getTransform(), !(isMouseOn || isFocused) ? 0xFFFFFFFF : 0xFFFFFF00, getWidth() / 2 - font.getTextLength(toDraw) / 2, getHeight() / 2 - font.getCharHeight('H') / 2 + 1, camera, engine);
		glLogicOp(GL_COPY);
		engine.disableGLCap(GL_COLOR_LOGIC_OP);
	}
}
