package org.jge.commands;

public class CommandArgument
{

	private String content;

	public CommandArgument(String content)
	{
		this.content = content;
	}

	public String getContentAsString()
	{
		return content;
	}

	public int getContentAsInt()
	{
		return Integer.parseInt(content);
	}

	public double getContentAsDouble()
	{
		return Double.parseDouble(content);
	}

	public float getContentAsFloat()
	{
		return Float.parseFloat(content);
	}

	public boolean getContentAsBoolean()
	{
		return Boolean.parseBoolean(content);
	}
}
