package org.jge.components.hud;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.jge.CoreEngine;
import org.jge.ResourceLocation;
import org.jge.components.Camera;
import org.jge.components.SceneObject;
import org.jge.components.hud.events.ClickEvent;
import org.jge.components.hud.events.FocusEvents.FocusGainedEvent;
import org.jge.components.hud.events.FocusEvents.FocusLostEvent;
import org.jge.events.Event;
import org.jge.events.EventBus;
import org.jge.events.EventListener;
import org.jge.game.Input;
import org.jge.maths.Transform;
import org.jge.render.Material;
import org.jge.render.RenderEngine;
import org.jge.render.Sprite;
import org.jge.render.Texture;
import org.jge.render.Vertex;
import org.jge.render.mesh.Mesh;
import org.jge.render.shaders.Shader;

public class HUDWidget extends SceneObject
{

	public static final ResourceLocation widgetLocation = new ResourceLocation("textures", "widgets.png");
	public static Texture				widgetTexture;
	private float						w;
	private float						h;
	protected Mesh					   renderMesh;
	protected Material				   renderMaterial;
	protected boolean					isMouseOn;
	protected boolean					isFocused;
	private HUDWidget					parent;

	private EventBus					 eventBus;
	private static Sprite				topLeft;
	private static Sprite				topRight;
	private static Sprite				bottomLeft;
	private static Sprite				bottomRight;
	private static Sprite				top;
	private static Sprite				bottom;
	private static Sprite				left;
	private static Sprite				right;
	private static Sprite				in;

	public HUDWidget(float w, float h)
	{
		this.eventBus = new EventBus();
		this.w = w;
		this.h = h;

		try
		{
			if(widgetTexture == null)
			{
				widgetTexture = new Texture(CoreEngine.getCurrent().getClasspathResourceLoader().getResource(widgetLocation), GL_NEAREST);
			}
			renderMesh = new Mesh();
			renderMaterial = new Material();
			renderMaterial.setTexture("diffuse", widgetTexture);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public HUDWidget getParent()
	{
		return parent;
	}

	public HashMap<Class<? extends Event>, ArrayList<EventListener>> getListeners()
	{
		HashMap<Class<? extends Event>, ArrayList<EventListener>> result = new HashMap<Class<? extends Event>, ArrayList<EventListener>>();
		HashMap<Class<? extends Event>, ArrayList<EventListener>> parentMap = new HashMap<Class<? extends Event>, ArrayList<EventListener>>();
		if(parent != null) parentMap = parent.getListeners();
		result.putAll(eventBus.getListeners());

		Iterator<Class<? extends Event>> it = parentMap.keySet().iterator();
		while(it.hasNext())
		{
			Class<? extends Event> eventClass = it.next();
			ArrayList<EventListener> list = parentMap.get(eventClass);
			if(!result.containsKey(eventClass)) result.put(eventClass, list);
			if(!list.isEmpty())
			{
				ArrayList<EventListener> newList = new ArrayList<EventListener>();
				newList.addAll(list);
				newList.addAll(result.get(eventClass));
				result.put(eventClass, newList);
			}
		}

		return result;
	}

	public void onAddToScene(SceneObject object)
	{
		if(object instanceof HUDWidget)
		{
			this.parent = (HUDWidget)object;
		}
	}

	public void drawBox(Shader shader, Camera camera, RenderEngine engine, float startX, float startY)
	{
		drawBox(shader, camera, getTransform(), renderMaterial, renderMesh, engine, startX, startY, getWidth(), getHeight());
	}

	public void drawBox(Shader shader, Camera camera, RenderEngine engine, float startX, float startY, float width, float height)
	{
		drawBox(shader, camera, getTransform(), renderMaterial, renderMesh, engine, startX, startY, width, height);
	}

	public static void drawBox(Shader shader, Camera camera, Transform transform, Material renderMaterial, Mesh renderMesh, RenderEngine engine, float startX, float startY, float width, float height)
	{
		if(topLeft == null)
		{
			topLeft = new Sprite(widgetTexture, startX + 0, startY + 0, 16, 16);
		}
		else
		{
			topLeft.setRegionMinX(startX + 0);
			topLeft.setRegionMinY(startY + 0);
			topLeft.setRegionMaxX(topLeft.getRegionMinX() + 16);
			topLeft.setRegionMaxY(topLeft.getRegionMinY() + 16);
		}

		if(topRight == null)
		{
			topRight = new Sprite(widgetTexture, startX + 16, startY + 0, 16, 16);
		}
		else
		{
			topRight.setRegionMinX(startX + 16);
			topRight.setRegionMinY(startY + 0);
			topRight.setRegionMaxX(topRight.getRegionMinX() + 16);
			topRight.setRegionMaxY(topRight.getRegionMinY() + 16);
		}
		if(bottomLeft == null)
		{
			bottomLeft = new Sprite(widgetTexture, startX, startY + 16, 16, 16);
		}
		else
		{
			bottomLeft.setRegionMinX(startX);
			bottomLeft.setRegionMinY(startY + 16);
			bottomLeft.setRegionMaxX(bottomLeft.getRegionMinX() + 16);
			bottomLeft.setRegionMaxY(bottomLeft.getRegionMinY() + 16);
		}

		if(bottomRight == null)
		{
			bottomRight = new Sprite(widgetTexture, startX + 16, startY + 16, 16, 16);
		}
		else
		{
			bottomRight.setRegionMinX(startX + 16);
			bottomRight.setRegionMinY(startY + 16);
			bottomRight.setRegionMaxX(bottomRight.getRegionMinX() + 16);
			bottomRight.setRegionMaxY(bottomRight.getRegionMinY() + 16);
		}

		if(top == null)
		{
			top = new Sprite(widgetTexture, startX + 32, startY + 0, 16, 16);
		}
		else
		{
			top.setRegionMinX(startX + 32);
			top.setRegionMinY(startY + 0);
			top.setRegionMaxX(top.getRegionMinX() + 16);
			top.setRegionMaxY(top.getRegionMinY() + 16);
		}

		if(bottom == null)
		{
			bottom = new Sprite(widgetTexture, startX + 32, startY + 16, 16, 16);
		}
		else
		{
			bottom.setRegionMinX(startX + 32);
			bottom.setRegionMinY(startY + 16);
			bottom.setRegionMaxX(bottom.getRegionMinX() + 16);
			bottom.setRegionMaxY(bottom.getRegionMinY() + 16);
		}

		if(left == null)
		{
			left = new Sprite(widgetTexture, startX + 48, startY + 0, 16, 16);
		}
		else
		{
			left.setRegionMinX(startX + 48);
			left.setRegionMinY(startY + 0);
			left.setRegionMaxX(left.getRegionMinX() + 16);
			left.setRegionMaxY(left.getRegionMinY() + 16);
		}
		if(right == null)
		{
			right = new Sprite(widgetTexture, startX + 48, startY + 16, 16, 16);
		}
		else
		{
			right.setRegionMinX(startX + 48);
			right.setRegionMinY(startY + 16);
			right.setRegionMaxX(right.getRegionMinX() + 16);
			right.setRegionMaxY(right.getRegionMinY() + 16);
		}

		if(in == null)
		{
			in = new Sprite(widgetTexture, startX + 0, startY + 32, 16, 16);
		}
		else
		{
			in.setRegionMinX(startX + 0);
			in.setRegionMinY(startY + 32);
			in.setRegionMaxX(in.getRegionMinX() + 16);
			in.setRegionMaxY(in.getRegionMinY() + 16);
		}

		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		int currentIndex = 0;

		for(int y = 16; y < height - 16; y += 16)
		{
			for(int x = 16; x < width - 16; x += 16)
			{
				currentIndex += in.prepareGroupedRendering(vertices, indices, currentIndex, x, y);
			}
		}

		for(int x = 16; x < width - 16; x += 16)
		{
			currentIndex += bottom.prepareGroupedRendering(vertices, indices, currentIndex, x, 0);
			currentIndex += top.prepareGroupedRendering(vertices, indices, currentIndex, x, height - 16);
		}

		for(int y = 16; y < height - 16; y += 16)
		{
			currentIndex += left.prepareGroupedRendering(vertices, indices, currentIndex, 0, y);
			currentIndex += right.prepareGroupedRendering(vertices, indices, currentIndex, width - 16, y);
		}

		currentIndex += bottomLeft.prepareGroupedRendering(vertices, indices, currentIndex, 0, 0);
		currentIndex += topLeft.prepareGroupedRendering(vertices, indices, currentIndex, 0, height - 16);
		currentIndex += bottomRight.prepareGroupedRendering(vertices, indices, currentIndex, width - 16, 0);
		currentIndex += topRight.prepareGroupedRendering(vertices, indices, currentIndex, width - 16, height - 16);

		shader.bind();
		Integer[] indicesArray = indices.toArray(new Integer[0]);
		Vertex[] verticesArray = vertices.toArray(new Vertex[0]);
		int[] indicesArrayInt = new int[indicesArray.length];
		for(int i = 0; i < indicesArray.length; i++ )
			indicesArrayInt[i] = indicesArray[i];
		renderMesh.setVertices(verticesArray, indicesArrayInt, false);
		renderMesh.sendDataToOGL();
		shader.updateUniforms(transform, camera, renderMaterial, engine);
		renderMesh.draw();
	}

	public boolean fireEvent(Event e)
	{
		return EventBus.fireEvent(e, getListeners());
	}

	public HUDWidget registerListener(Object object)
	{
		eventBus.registerListener(object);
		return this;
	}

	public boolean hasListener(Object object)
	{
		return eventBus.hasListener(object);
	}

	public void update(double delta)
	{
		super.update(delta);
		this.isMouseOn = isMouseOn(Input.getMouseX(), Input.getMouseY());

		if(Input.isButtonJustPressed(0) && isMouseOn)
		{
			if(!fireEvent(new FocusGainedEvent(this, Input.getMouseX(), Input.getMouseY()))) this.isFocused = true;
		}

		if(Input.isButtonJustReleased(0) && isFocused)
		{
			if(isMouseOn)
			{
				fireEvent(new ClickEvent(this, Input.getMouseX(), Input.getMouseY()));
			}
			if(!fireEvent(new FocusLostEvent(this, Input.getMouseX(), Input.getMouseY()))) this.isFocused = false;
		}
	}

	public float getWidth()
	{
		return w;
	}

	public HUDWidget setWidth(float newW)
	{
		w = newW;
		return this;
	}

	public float getHeight()
	{
		return h;
	}

	public HUDWidget setHeight(float newH)
	{
		h = newH;
		return this;
	}

	public boolean isMouseOn(int mx, int my)
	{
		return mx >= getTransform().getTransformedPos().x && my >= getTransform().getTransformedPos().y && mx <= getTransform().getTransformedPos().x + w && my <= getTransform().getTransformedPos().y + h;
	}
}
