package org.jge.components;

import org.jge.maths.Matrix4;
import org.jge.maths.Vector3;

public class Camera extends SceneComponent
{

	private Matrix4 projection;

	private String  name = "unknown cam";

	public Camera(Matrix4 projection)
	{
		this.projection = projection;
	}

	public Camera(float fov, float aspect, float zNear, float zFar)
	{
		super();
		this.projection = new Matrix4().initPerspective(fov, aspect, zNear, zFar);
	}

	public Camera setProjection(Matrix4 projection)
	{
		this.projection = projection;
		return this;
	}

	public Matrix4 getViewProjection()
	{
		Matrix4 cameraRotation = getRotationMatrix();
		Vector3 cameraPos = getParent().getTransform().getTransformedPos().mul(-1);
		Matrix4 cameraTranslation = new Matrix4().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
		return projection.mul(cameraRotation.mul(cameraTranslation));
	}

	public Matrix4 getRotationMatrix()
	{
		return getParent().getTransform().getTransformedRotation().conjugate().toRotationMatrix();
	}

	public void init(SceneObject object)
	{
		super.init(object);
	}

	public Vector3 getLeft()
	{
		return getParent().getTransform().getRotation().getLeft();
	}

	public Vector3 getRight()
	{
		return getParent().getTransform().getRotation().getRight();
	}

	public Vector3 getPos()
	{
		return getParent().getTransform().getTransformedPos();
	}

	public void setPos(Vector3 pos)
	{
		getParent().getTransform().setPosition(pos);
	}

	public Vector3 getForward()
	{
		return getParent().getTransform().getTransformedRotation().getForward();
	}

	public Vector3 getUp()
	{
		return getParent().getTransform().getTransformedRotation().getUp();
	}

	public String getName()
	{
		return name;
	}

	public Camera setName(String name)
	{
		this.name = name;
		return this;
	}

}
