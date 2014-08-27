package org.jge.newphys;

import org.jge.maths.Vector3;

public class AABB
{

	private Vector3 minExtents;
	private Vector3 maxExtents;

	public AABB(Vector3 minExtents, Vector3 maxExtents)
	{
		this.minExtents = minExtents;
		this.maxExtents = maxExtents;
	}

	public IntersectData intersectAABB(AABB other)
	{
		Vector3 dist1 = other.getMinExtents().sub(getMaxExtents());
		Vector3 dist2 = getMinExtents().sub(other.getMaxExtents());
		Vector3 dist = Vector3.max(dist1, dist2);
		double maxDistance = dist.max();

		return new IntersectData(maxDistance < 0, (float)maxDistance);
	}

	public Vector3 getMinExtents()
	{
		return minExtents;
	}

	public Vector3 getMaxExtents()
	{
		return maxExtents;
	}
}
