package org.jge.newphys;

public class IntersectData
{

	private boolean doesIntersects;
	private float   distance;
	
	public IntersectData(boolean intersects, float distance)
	{
		this.doesIntersects = intersects;
		this.distance = distance;
	}
	
	public float getDistance()
	{
		return distance;
	}
	
	public boolean doesIntersects()
	{
		return doesIntersects;
	}
	
}
