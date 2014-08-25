package org.jge.tests;

import org.jge.components.Camera;
import org.jge.components.SceneObject;
import org.jge.components.hud.HUD3DView;
import org.jge.components.hud.HUDButton;
import org.jge.components.hud.HUDCheckBox;
import org.jge.components.hud.HUDMenu;
import org.jge.components.hud.events.ClickEvent;
import org.jge.events.Event.EventSubscribe;
import org.jge.maths.Vector3;
import org.jge.render.fonts.SimpleFont;
import org.jge.util.Log;

public class TestMenu extends HUDMenu
{

	private HUD3DView view;

	public TestMenu()
	{
		super();
		
		HUDButton button = new HUDButton(200,40, "Hi", SimpleFont.instance);
		button.getTransform().setPosition(new Vector3(300, 50, 0));
		addChild(button);
		
		HUDCheckBox checkBox = new HUDCheckBox(32,32);
		checkBox.getTransform().setPosition(new Vector3(200, 50, 0));
		addChild(checkBox);
		
		view = new HUD3DView(400,400);
		view.getTransform().setPosition(new Vector3(500, 50, 0));
		addChild(view);
		
		registerListener(this);
	}
	
	public void setViewRoot(SceneObject root)
	{
		view.setViewRoot(root);
	}
	
	public void setViewCam(Camera cam)
	{
		view.setViewCamera(cam);
	}
	
	@EventSubscribe
	public void onClicked(ClickEvent event)
	{
		Log.message("Click on "+event.getSource().getClass().getSimpleName()+" at "+event.x+";"+event.y);
	}
}
