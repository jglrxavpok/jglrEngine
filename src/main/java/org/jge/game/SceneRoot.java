package org.jge.game;

import org.jge.CoreEngine;
import org.jge.components.SceneObject;

public class SceneRoot extends SceneObject
{

	public SceneRoot()
	{
		addChildAs("world", new WorldObject());
		if(!CoreEngine.getCurrent().updateOnly) addChildAs("hud", new HUDObject());
	}
}
