package org.jge.newphys;

import org.jge.maths.Vector3;

public class BoundingSphere
{

	private Vector3 center;
	private float radius;
	
	public BoundingSphere(Vector3 center, float radius)
	{
		this.center = center;
		this.radius = radius;
	}
	
	public Vector3 getCenter()
	{
		return center;
	}
	
	public float getRadius()
	{
		return radius;
	}
	
	public IntersectData intersectBoundingSphere(BoundingSphere other)
	{
		float radiusDistance = radius + other.getRadius();
		float centerDistance = (float)center.sub(other.center).length();
		
		if(centerDistance < radiusDistance)
		{
			return new IntersectData(true, centerDistance-radiusDistance);
		}
		else
			return new IntersectData(false, centerDistance-radiusDistance);
	}
}
