package org.jge.script.lua;

import java.util.HashMap;

import org.jge.CoreEngine;
import org.jge.EngineException;
import org.jge.components.SceneComponent;
import org.jge.components.SceneObject;
import org.jge.tests.SceneComponentTest;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 jglrxavpok
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class EngineLib extends TwoArgFunction
{

	private static HashMap<LuaValue, SceneObject>	objectMap	 = new HashMap<LuaValue, SceneObject>();

	private static HashMap<LuaValue, SceneComponent> componentsMap = new HashMap<LuaValue, SceneComponent>();

	public class NewSceneObjectFunc extends LuaFunction
	{
		public LuaValue call()
		{
			LuaTable sceneObject = new LuaTable();
			SceneObject sceneObjectLocal = new SceneObject();
			objectMap.put(sceneObject, sceneObjectLocal);
			// TODO: Add transform stuff
			return sceneObject;
		}
	}

	public class NewSceneComponentTestFunc extends LuaFunction
	{
		public LuaValue call()
		{
			LuaTable sceneObject = new LuaTable();
			SceneComponent sceneObjectLocal;
			try
			{
				sceneObjectLocal = new SceneComponentTest();
				componentsMap.put(sceneObject, sceneObjectLocal);
			}
			catch(EngineException e)
			{
				e.printStackTrace();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return sceneObject;
		}
	}

	public class AddComponentFunc extends ThreeArgFunction
	{

		@Override
		public LuaValue call(LuaValue name, LuaValue object, LuaValue component)
		{
			String compName = name.toString();
			SceneObject savedObject = objectMap.get(object);
			SceneComponent savedComp = componentsMap.get(component);
			savedObject.addComponentAs(compName, savedComp);
			return NIL;
		}
	}

	private class AddToWorldFunc extends TwoArgFunction
	{

		@Override
		public LuaValue call(LuaValue name, LuaValue object)
		{
			String objectName = name.toString();
			SceneObject sceneObject = objectMap.get(object);
			CoreEngine.getCurrent().getGame().addToWorld(objectName, sceneObject);
			return NIL;
		}

	}

	@Override
	public LuaValue call(LuaValue par1, LuaValue par2)
	{
		LuaTable table = new LuaTable();
		table.set("SceneObject", new NewSceneObjectFunc());
		table.set("SceneComponentTest", new NewSceneComponentTestFunc());
		table.set("addToWorld", new AddToWorldFunc());
		table.set("addComponent", new AddComponentFunc());
		par2.set("Engine", table);
		par2.get("package").get("loaded").set("Engine", table);
		return NIL;
	}

}
