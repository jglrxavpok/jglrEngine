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
	private double w;
	private double h;
	protected Mesh renderMesh;
	protected Material renderMaterial;
	protected Texture widgetTexture;
	protected boolean isMouseOn;
	protected boolean isFocused;
	private HUDWidget parent;
	
	private EventBus eventBus;
	
	public HUDWidget(double w, double h)
	{
		this.eventBus = new EventBus();
		this.w = w;
		this.h = h;
		
		this.renderMesh = new Mesh();
		this.renderMaterial = new Material();
		try
		{
			this.widgetTexture = new Texture(CoreEngine.getCurrent().getClasspathResourceLoader().getResource(widgetLocation), GL_NEAREST);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		renderMaterial.setTexture("diffuse", widgetTexture);
	}
	
	public HUDWidget getParent()
	{
		return parent;
	}
	
	public HashMap<Class<? extends Event>, ArrayList<EventListener>> getListeners()
	{
		HashMap<Class<? extends Event>, ArrayList<EventListener>> result = new HashMap<Class<? extends Event>, ArrayList<EventListener>>();
		HashMap<Class<? extends Event>, ArrayList<EventListener>> parentMap = new HashMap<Class<? extends Event>, ArrayList<EventListener>>();
		if(parent != null)
			parentMap = parent.getListeners();
		result.putAll(eventBus.getListeners());
		
		Iterator<Class<? extends Event>> it = parentMap.keySet().iterator();
		while(it.hasNext())
		{
			Class<? extends Event> eventClass = it.next();
			ArrayList<EventListener> list = parentMap.get(eventClass);
			if(!result.containsKey(eventClass))
				result.put(eventClass, list);
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
	
	public void drawBox(Shader shader, Camera camera, RenderEngine engine, double startX, double startY)
	{
		Sprite topLeft = new Sprite(this.widgetTexture, startX+0,startY+0,16,16);
		Sprite topRight = new Sprite(this.widgetTexture, startX+16,startY+0,16,16);
		Sprite bottomLeft = new Sprite(this.widgetTexture, startX+0,startY+16,16,16);
		Sprite bottomRight = new Sprite(this.widgetTexture, startX+16,startY+16,16,16);
		Sprite top = new Sprite(this.widgetTexture, startX+32,startY+0,16,16);
		Sprite bottom = new Sprite(this.widgetTexture, startX+32,startY+16,16,16);
		Sprite left = new Sprite(this.widgetTexture, startX+48,startY+0,16,16);
		Sprite right = new Sprite(this.widgetTexture, startX+48,startY+16,16,16);
		Sprite in = new Sprite(this.widgetTexture, startX+0,startY+32,16,16);
		
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		int currentIndex = 0;
		
		for(int y = 16;y<getHeight()-16;y+=16)
		{
			for(int x = 16;x<getWidth()-16;x+=16)
			{
				currentIndex += in.prepareGroupedRendering(vertices, indices, currentIndex, x, y);
			}
		}

		for(int x = 16;x<getWidth()-16;x+=16)
		{
			currentIndex += bottom.prepareGroupedRendering(vertices, indices, currentIndex, x, 0);
			currentIndex += top.prepareGroupedRendering(vertices, indices, currentIndex, x, getHeight()-16);
		}
		
		for(int y = 16;y<getHeight()-16;y+=16)
		{
			currentIndex += left.prepareGroupedRendering(vertices, indices, currentIndex, 0, y);
			currentIndex += right.prepareGroupedRendering(vertices, indices, currentIndex, getWidth()-16, y);
		}
		
		
		currentIndex += bottomLeft.prepareGroupedRendering(vertices, indices, currentIndex, 0, 0);
		currentIndex += topLeft.prepareGroupedRendering(vertices, indices, currentIndex, 0, getHeight()-16);
		currentIndex += bottomRight.prepareGroupedRendering(vertices, indices, currentIndex, getWidth()-16, 0);
		currentIndex += topRight.prepareGroupedRendering(vertices, indices, currentIndex, getWidth()-16, getHeight()-16);
		
		shader.bind();
		Integer[] indicesArray = indices.toArray(new Integer[0]);
        Vertex[] verticesArray = vertices.toArray(new Vertex[0]);
        int[] indicesArrayInt = new int[indicesArray.length];
        for(int i = 0;i<indicesArray.length;i++)
            indicesArrayInt[i] = indicesArray[i];
        renderMesh.setVertices(verticesArray, indicesArrayInt, false);
        renderMesh.sendDataToOGL();
        shader.updateUniforms(getTransform(), camera, renderMaterial, engine);
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
			if(!fireEvent(new FocusGainedEvent(this, Input.getMouseX(), Input.getMouseY())))
				this.isFocused = true;
		}
		
		if(Input.isButtonJustReleased(0) && isFocused)
		{
			if(isMouseOn)
			{
				fireEvent(new ClickEvent(this, Input.getMouseX(), Input.getMouseY()));
			}
			if(!fireEvent(new FocusLostEvent(this, Input.getMouseX(), Input.getMouseY())))
				this.isFocused = false;
		}
	}
	
	public double getWidth()
	{
		return w;
	}
	
	public HUDWidget setWidth(double newW)
	{
		w = newW;
		return this;
	}
	
	public double getHeight()
	{
		return h;
	}
	
	public HUDWidget setHeight(double newH)
	{
		h = newH;
		return this;
	}
	
	public boolean isMouseOn(int mx, int my)
	{
		return mx >= getTransform().getTransformedPos().x && my >= getTransform().getTransformedPos().y && mx <= getTransform().getTransformedPos().x+w && my <= getTransform().getTransformedPos().y+h;
	}
}
