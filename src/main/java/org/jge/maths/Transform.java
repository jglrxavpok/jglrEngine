package org.jge.maths;

public class Transform
{

	public static final Transform NULL   = new Transform();
	public static final Transform GLOBAL = new Transform();

	private Quaternion			rotation;
	private Vector3			   scale;
	private Vector3			   position;

	private Quaternion			oldRotation;
	private Vector3			   oldScale;
	private Vector3			   oldPosition;

	private Matrix4			   parentMatrix;
	private Transform			 parent;

	public Transform()
	{
		rotation = Quaternion.NULL;
		scale = Vector3.get(1, 1, 1);
		position = Vector3.NULL;
		parentMatrix = new Matrix4().initIdentity();
	}

	public Transform rotate(Vector3 axis, float angle)
	{
		rotation = new Quaternion(axis, angle).mul(rotation).normalize();
		return this;
	}

	public void lookAt(Vector3 point, Vector3 up)
	{
		rotation = getLookAtRotation(point, up);
	}

	public Quaternion getLookAtRotation(Vector3 point, Vector3 up)
	{
		Vector3 forward = point.sub(position).normalize();
		return new Quaternion(new Matrix4().initRotation(forward, up));
	}

	public boolean hasChanged()
	{
		if(parent != null && parent.hasChanged()) return true;

		if(!position.equals(oldPosition)) return true;

		if(!scale.equals(oldScale)) return true;

		if(!rotation.equals(oldRotation)) return true;

		return false;
	}

	private Matrix4 getParentMatrix()
	{
		if(parent != null && hasChanged()) parentMatrix = parent.getTransformationMatrix();
		return parentMatrix;
	}

	public void update()
	{
		if(oldPosition != null)
		{
			oldPosition = position;
			oldScale = scale;
			oldRotation = rotation;
		}
		else
		{
			oldPosition = position.add(1.0f);
			oldRotation = rotation.mul(0.5f);
			oldScale = scale.add(0.5f);
		}
	}

	public Matrix4 getTransformationMatrix()
	{
		Matrix4 translationMatrix = new Matrix4().initTranslation(position.getX(), position.getY(), position.getZ());
		Matrix4 rotationMatrix = rotation.toRotationMatrix();
		Matrix4 scalingMatrix = new Matrix4().initScale(scale.getX(), scale.getY(), scale.getZ());
		return getParentMatrix().mul(translationMatrix.mul(rotationMatrix.mul(scalingMatrix)));
	}

	public Vector3 getTransformedPos()
	{
		return getParentMatrix().transform(position);
	}

	public Vector3 getTransformedScale()
	{
		return getParentMatrix().transform(scale);
	}

	public Quaternion getTransformedRotation()
	{
		Quaternion parentRotation = Quaternion.NULL;
		if(parent != null)
		{
			parentRotation = getParent().getRotation();
		}
		return parentRotation.mul(rotation);
	}

	public Quaternion getRotation()
	{
		return rotation;
	}

	public void translate(double x, double y, double z)
	{
		this.position.x += x;
		this.position.y += y;
		this.position.z += z;
	}

	public void scale(double x, double y, double z)
	{
		this.scale.x *= x;
		this.scale.y *= y;
		this.scale.z *= z;
	}

	public Vector3 getScaling()
	{
		return scale;
	}

	public Vector3 getPosition()
	{
		return position;
	}

	public void setRotation(Quaternion rotation)
	{
		this.rotation = rotation.copy();
	}

	public void setPosition(Vector3 pos)
	{
		this.position = pos.copy();
	}

	public Vector3 getScale()
	{
		return scale;
	}

	public void setScale(Vector3 scale)
	{
		this.scale = scale;
	}

	public Transform getParent()
	{
		return parent;
	}

	public Transform setParent(Transform parent)
	{
		this.parent = parent;
		parentMatrix = parent.getTransformationMatrix();
		return this;
	}

	public Vector3 transform(Vector3 v)
	{
		return getTransformationMatrix().transform(v);
	}

	public void setRotation(Matrix4 rotMatrix)
	{
		this.rotation = new Quaternion(rotMatrix);
	}

	public void forward(float f)
	{
		this.position = position.add(getRotation().getForward().copy().setY(0).normalize().mul(f));
	}

	public void backwards(float f)
	{
		forward(-1f * f);
	}

	public void strafeRight(float f)
	{
		this.position = position.add(getRotation().getRight().mul(f));
	}

	public void strafeLeft(float f)
	{
		strafeRight(f * -1f);
	}

}
