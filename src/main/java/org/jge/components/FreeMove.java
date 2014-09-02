package org.jge.components;

import org.jge.game.Input;
import org.jge.maths.Vector3;

public class FreeMove extends SceneComponent
{

	public static final boolean  AZERTY_KEYBOARD = true;
	private static final Vector3 yAxis		   = Vector3.get(0, 1, 0);
	private double			   speed;

	/**
	 * @param speed
	 *            : in m/s!
	 */
	public FreeMove(float speed)
	{
		this.speed = speed;
	}

	public void moveForward(float amnt)
	{
		this.move(getParent().getTransform().getRotation().getForward(), amnt);
	}

	public void moveBackwards(float amnt)
	{
		this.move(getParent().getTransform().getRotation().getForward().negative(), amnt);
	}

	public void strafeLeft(float amnt)
	{
		Vector3 left = getParent().getTransform().getRotation().getLeft();
		move(left, amnt);
	}

	public void strafeRight(float amnt)
	{
		Vector3 right = getParent().getTransform().getRotation().getRight();
		move(right, amnt);
	}

	public void scrollUp(float amnt)
	{
		move(yAxis, amnt);
	}

	public void scrollDown(float amnt)
	{
		move(yAxis.negative(), amnt);
	}

	public void moveDown(float amnt)
	{
		move(getParent().getTransform().getRotation().getUp().negative(), amnt);
	}

	public void moveUp(float amnt)
	{
		move(getParent().getTransform().getRotation().getUp(), amnt);
	}

	public void update(double delta)
	{
		float speed = (float)(this.speed * delta * 60);
		if(Input.isKeyDown(Input.KEY_D))
		{
			strafeRight(speed);
		}

		if(Input.isKeyDown(AZERTY_KEYBOARD ? Input.KEY_Q : Input.KEY_A))
		{
			strafeLeft(speed);
		}

		if(Input.isKeyDown(AZERTY_KEYBOARD ? Input.KEY_Z : Input.KEY_W))
		{
			moveForward(speed);
		}

		if(Input.isKeyDown(Input.KEY_S))
		{
			moveBackwards(speed);
		}

		if(Input.isKeyDown(Input.KEY_SPACE))
		{
			scrollUp(speed);
		}

		if(Input.isKeyDown(Input.KEY_LSHIFT))
		{
			scrollDown(speed);
		}
	}

	public void move(Vector3 dir, float amt)
	{
		getParent().getTransform().setPosition(getParent().getTransform().getPosition().add(dir.mul(amt)));
	}
}
