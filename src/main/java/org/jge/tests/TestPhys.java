package org.jge.tests;

import org.jge.maths.Vector3;
import org.jge.newphys.AABB;
import org.jge.newphys.BoundingSphere;
import org.jge.newphys.IntersectData;

public class TestPhys
{

	public static void main(String[] args)
	{
		BoundingSphere sphere1 = new BoundingSphere(Vector3.get(0, 0, 0), 1);
		BoundingSphere sphere2 = new BoundingSphere(Vector3.get(0, 3, 0), 1);
		BoundingSphere sphere3 = new BoundingSphere(Vector3.get(0, 0, 2), 1);
		BoundingSphere sphere4 = new BoundingSphere(Vector3.get(1, 0, 0), 1);

		IntersectData intersectsSphere2 = sphere1.intersectBoundingSphere(sphere2);
		IntersectData intersectsSphere3 = sphere1.intersectBoundingSphere(sphere3);
		IntersectData intersectsSphere4 = sphere1.intersectBoundingSphere(sphere4);
		System.out.println("Sphere1 intersects Sphere2 ? " + intersectsSphere2.doesIntersects() + ", Distance: " + intersectsSphere2.getDistance());
		System.out.println("Sphere1 intersects Sphere3 ? " + intersectsSphere3.doesIntersects() + ", Distance: " + intersectsSphere3.getDistance());
		System.out.println("Sphere1 intersects Sphere4 ? " + intersectsSphere4.doesIntersects() + ", Distance: " + intersectsSphere4.getDistance());

		AABB aabb1 = new AABB(Vector3.get(0, 0, 0), Vector3.get(1, 1, 1));
		AABB aabb2 = new AABB(Vector3.get(1, 1, 1), Vector3.get(2, 2, 2));
		AABB aabb3 = new AABB(Vector3.get(1, 0, 0), Vector3.get(2, 1, 1));
		AABB aabb4 = new AABB(Vector3.get(0, 0, -2), Vector3.get(1, 1, -1));
		AABB aabb5 = new AABB(Vector3.get(0, 0.5f, 0), Vector3.get(1, 1.5f, 1));

		IntersectData intersectsAABB2 = aabb1.intersectAABB(aabb2);
		IntersectData intersectsAABB3 = aabb1.intersectAABB(aabb3);
		IntersectData intersectsAABB4 = aabb1.intersectAABB(aabb4);
		IntersectData intersectsAABB5 = aabb1.intersectAABB(aabb5);
		System.out.println("AABB1 intersects AABB2 ? " + intersectsAABB2.doesIntersects() + ", Distance: " + intersectsAABB2.getDistance());
		System.out.println("AABB1 intersects AABB3 ? " + intersectsAABB3.doesIntersects() + ", Distance: " + intersectsAABB3.getDistance());
		System.out.println("AABB1 intersects AABB4 ? " + intersectsAABB4.doesIntersects() + ", Distance: " + intersectsAABB4.getDistance());
		System.out.println("AABB1 intersects AABB5 ? " + intersectsAABB5.doesIntersects() + ", Distance: " + intersectsAABB5.getDistance());
	}

}
