package org.jge.render;

public class TextureRegion implements TextureIcon
{

	private Texture texture;
	private double  minU;
	private double  minV;
	private double  maxU;
	private double  maxV;

	public TextureRegion(Texture texture)
	{
		this(texture, 0, 0, 1, 1);
	}

	public TextureRegion(Texture texture, TextureIcon icon)
	{
		this(texture, icon.getMinU(), icon.getMinV(), icon.getMaxU(), icon.getMaxV());
	}

	public TextureRegion(Texture texture, double minU, double minV, double maxU, double maxV)
	{
		this.texture = texture;
		this.minU = minU;
		this.minV = minV;
		this.maxU = maxU;
		this.maxV = maxV;
	}

	public Texture getTexture()
	{
		return texture;
	}

	public double getMinU()
	{
		return minU;
	}

	public double getMinV()
	{
		return minV;
	}

	public double getMaxU()
	{
		return maxU;
	}

	public double getMaxV()
	{
		return maxV;
	}

	public TextureRegion setTexture(Texture text)
	{
		this.texture = text;
		return this;
	}

	public TextureRegion setMinV(double newMinV)
	{
		this.minV = newMinV;
		return this;
	}

	public TextureRegion setMaxU(double newMaxU)
	{
		this.maxU = newMaxU;
		return this;
	}

	public TextureRegion setMinU(double newMinU)
	{
		this.minU = newMinU;
		return this;
	}

	public TextureRegion setMaxV(double newMaxV)
	{
		this.maxV = newMaxV;
		return this;
	}

	public TextureRegion flip(boolean horizontaly, boolean verticaly)
	{
		if(horizontaly)
		{
			double oldMaxU = maxU;
			maxU = minU;
			minU = oldMaxU;
		}

		if(verticaly)
		{
			double oldMaxV = maxV;
			maxV = minV;
			minV = oldMaxV;
		}
		return this;
	}

	@Override
	public double getWidth()
	{
		return (double)((getMaxU() - getMinU()) * texture.getWidth());
	}

	@Override
	public double getHeight()
	{
		return (double)((getMaxV() - getMinV()) * texture.getHeight());
	}
}
