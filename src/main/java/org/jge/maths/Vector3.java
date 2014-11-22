package org.jge.maths;

import java.nio.FloatBuffer;

import org.jge.util.BufferWritable;

import org.jglrxavpok.jlsl.glsl.GLSL.Substitute;

public class Vector3 implements BufferWritable
{

	public static final Vector3 NULL  = new Vector3(0, 0, 0);

	public static final Vector3 yAxis = new Vector3(0, 1, 0);

	public static final Vector3 xAxis = new Vector3(1, 0, 0);

	public static final Vector3 zAxis = new Vector3(0, 0, 1);

	public float				x;
	public float				y;
	public float				z;

	private Vector3(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3()
	{
	}

	public String toString()
	{
		return "vec3(" + x + "," + y + "," + z + ")";
	}

	public Vector3 normalize()
	{
		float l = length();
		if(l == 0) return Vector3.NULL;
		float _x = this.x / l;
		float _y = this.y / l;
		float _z = this.z / l;
		return Vector3.get(_x, _y, _z);
	}

	/**
	 * @param v
	 * @return
	 */
	public float dot(Vector3 r)
	{
		return x * r.x + y * r.y + z * r.z;
	}

	public Vector3 cross(Vector3 v)
	{
		float x = this.y * v.z - this.z * v.y;
		float y = this.z * v.x - this.x * v.z;
		float z = this.x * v.y - this.y * v.x;
		return Vector3.get(x, y, z);
	}

	@Substitute(value = "+", ownerBefore = true, usesParenthesis = false)
	public Vector3 add(float factor)
	{
		return Vector3.get(this.x + factor, this.y + factor, this.z + factor);
	}

	@Substitute(value = "+", ownerBefore = true, usesParenthesis = false)
	public Vector3 add(float x, float y, float z)
	{
		return Vector3.get(this.x + x, this.y + y, this.z + z);
	}

	@Substitute(value = "+", ownerBefore = true, usesParenthesis = false)
	public Vector3 add(Vector3 v)
	{
		return Vector3.get(this.x + v.x, this.y + v.y, this.z + v.z);
	}

	@Substitute(value = "-", ownerBefore = true, usesParenthesis = false)
	public Vector3 sub(float factor)
	{
		return Vector3.get(this.x - factor, this.y - factor, this.z - factor);
	}

	@Substitute(value = "-", ownerBefore = true, usesParenthesis = false)
	public Vector3 sub(float x, float y, float z)
	{
		return Vector3.get(this.x - x, this.y - y, this.z - z);
	}

	@Substitute(value = "-", ownerBefore = true, usesParenthesis = false)
	public Vector3 sub(Vector3 v)
	{
		return Vector3.get(this.x - v.x, this.y - v.y, this.z - v.z);
	}

	@Substitute(value = "/", ownerBefore = true, usesParenthesis = false)
	public Vector3 div(float factor)
	{
		return Vector3.get(this.x / factor, this.y / factor, this.z / factor);
	}

	@Substitute(value = "/", ownerBefore = true, usesParenthesis = false)
	public Vector3 div(float x, float y, float z)
	{
		return Vector3.get(this.x / x, this.y / y, this.z / z);
	}

	@Substitute(value = "/", ownerBefore = true, usesParenthesis = false)
	public Vector3 div(Vector3 v)
	{
		return Vector3.get(this.x / v.x, this.y / v.y, this.z / v.z);
	}

	@Substitute(value = "*", ownerBefore = true, usesParenthesis = false)
	public Vector3 mul(float factor)
	{
		return Vector3.get(this.x * factor, this.y * factor, this.z * factor);
	}

	@Substitute(value = "*", ownerBefore = true, usesParenthesis = false)
	public Vector3 mul(Vector3 v)
	{
		return Vector3.get(this.x * v.x, this.y * v.y, this.z * v.z);
	}

	@Substitute(value = "*", ownerBefore = true, usesParenthesis = false)
	public Vector3 mul(float x, float y, float z)
	{
		return Vector3.get(this.x * x, this.y * y, this.z * z);
	}

	public float length()
	{
		return (float)Maths.sqrt(x * x + y * y + z * z);
	}

	public Vector3 set(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	@Substitute(value = "x", actsAsField = true)
	public float getX()
	{
		return x;
	}

	@Substitute(value = "x", actsAsField = true)
	public float getY()
	{
		return y;
	}

	@Substitute(value = "x", actsAsField = true)
	public float getZ()
	{
		return z;
	}

	public void write(FloatBuffer buffer)
	{
		buffer.put((float)getX());
		buffer.put((float)getY());
		buffer.put((float)getZ());
	}

	public int getSize()
	{
		return 3;
	}

	public Vector3 rotate(float angle, Vector3 axis)
	{
		float sinAngle = (float)Math.sin(-angle);
		float cosAngle = (float)Math.cos(-angle);
		return this.cross(axis.mul(sinAngle)).add( // Rotation on local X
				(this.mul(cosAngle)).add( // Rotation on local Z
						axis.mul(this.dot(axis.mul(1 - cosAngle))))); // Rotation
																	  // on
																	  // local Y
	}

	public Vector3 rotate(Quaternion rotation)
	{
		Quaternion conjugate = rotation.conjugate();

		Quaternion w = rotation.mul(copy()).mul(conjugate);

		return Vector3.get(w.getX(), w.getY(), w.getZ());
	}

	public Vector3 copy()
	{
		return new Vector3(x, y, z);
	}

	public Vector3 negative()
	{
		return Vector3.get(-x, -y, -z);
	}

	@Substitute(value = "$", actsAsField = true)
	public Vector2 xy()
	{
		return new Vector2(x, y);
	}

	@Substitute(value = "$", actsAsField = true)
	public Vector2 xz()
	{
		return new Vector2(x, z);
	}

	@Substitute(value = "$", actsAsField = true)
	public Vector2 zx()
	{
		return new Vector2(z, x);
	}

	@Substitute(value = "$", actsAsField = true)
	public Vector2 yx()
	{
		return new Vector2(y, x);
	}

	@Substitute(value = "$", actsAsField = true)
	public Vector2 zy()
	{
		return new Vector2(z, y);
	}

	@Substitute(value = "$", actsAsField = true)
	public Vector2 yz()
	{
		return new Vector2(y, z);
	}

	public Vector3 lerp(Vector3 dest, float factor)
	{
		return dest.sub(this).mul(factor).add(this);
	}

	public float max()
	{
		return (float)Maths.max(getX(), Maths.max(getY(), getZ()));
	}

	public Vector3 set(Vector3 v)
	{
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
		return this;
	}

	@Substitute(value = "x", actsAsField = true)
	public Vector3 setX(float x)
	{
		this.x = x;
		return this;
	}

	@Substitute(value = "y", actsAsField = true)
	public Vector3 setY(float y)
	{
		this.y = y;
		return this;
	}

	@Substitute(value = "z", actsAsField = true)
	public Vector3 setZ(float z)
	{
		this.z = z;
		return this;
	}

	public boolean equals(Object o)
	{
		if(o == null) return false;
		if(o instanceof Vector3)
		{
			Vector3 v = (Vector3)o;
			return v.x == x && v.y == y && v.z == z;
		}
		if(o instanceof float[])
		{
			float[] array = (float[])o;
			return array[0] == x && array[1] == y && array[2] == z;
		}
		return false;
	}

	public static Vector3 cross(Vector3 a, Vector3 b)
	{
		return a.cross(b);
	}

	public static float dot(Vector3 a, Vector3 b)
	{
		return a.dot(b);
	}

	public static Vector3 max(Vector3 a, Vector3 b)
	{
		Vector3 vec3 = new Vector3();
		vec3.x = (float)Maths.max(a.x, b.x);
		vec3.y = (float)Maths.max(a.y, b.y);
		vec3.z = (float)Maths.max(a.z, b.z);
		return vec3;
	}

	@Substitute(value = "")
	public static Vector3 get(float x, float y, float z)
	{
		return new Vector3(x, y, z);
	}
}
