package org.jge.components;

import org.jge.game.Input;
import org.jge.maths.Maths;
import org.jge.maths.Vector3;

public class FreeLook extends SceneComponent
{

    private static final Vector3 yAxis = new Vector3(0,1,0);
	private double sensitivity;
	private boolean onlyOnMousePressed;
    
    public FreeLook(double sensitivity)
    {
    	this(sensitivity, true);
    }

	public FreeLook(double sensitivity, boolean onlyOnMousePressed)
	{
		this.sensitivity = sensitivity;
		this.onlyOnMousePressed = onlyOnMousePressed;
	}

	public void update(double delta)
    {
		if(!onlyOnMousePressed)
			Input.lockCursor(true);
		if(Input.isButtonPressed(0))
		{
			Input.lockCursor(true);
			getParent().getTransform().rotate(yAxis, Maths.toRadians(Input.getMouseDX()* sensitivity));
			getParent().getTransform().rotate(getParent().getTransform().getRotation().getRight(), Maths.toRadians(-Input.getMouseDY() * sensitivity));
		}
		else
			Input.lockCursor(false);
    }
}
