package org.jge.events;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.jge.events.Event.EventSubscribe;
import org.jge.util.Log;

public class EventBus
{

	private HashMap<Class<? extends Event>, ArrayList<EventListener>> listeners = new HashMap<Class<? extends Event>, ArrayList<EventListener>>();
	
	public EventBus()
	{
		
	}

	public HashMap<Class<? extends Event>, ArrayList<EventListener>> getListeners()
	{
		return listeners;
	}

	public boolean fireEvent(Event e)
	{
		return fireEvent(e, getListeners());
	}

	public static boolean fireEvent(Event e, HashMap<Class<? extends Event>, ArrayList<EventListener>> listenersMap)
	{
		if(listenersMap.containsKey(e.getClass()))
		{
			ArrayList<EventListener> list = listenersMap.get(e.getClass());
			if(!list.isEmpty())
			{
				for(EventListener listener : list)
				{
					if(listener.isEnabled())
    					try
    					{
    						Method m = listener.getListener().getClass().getDeclaredMethod(listener.getMethodName(), e.getClass());
    						m.setAccessible(true);
    						m.invoke(listener.getListener(), e);
    						listener.disable();
    					}
    					catch(Exception e1)
    					{
    						e1.printStackTrace();
    					}
				}
				for(EventListener listener : list)
				{
					listener.enable();
				}
			}
		}
		return e.isCancelled() && e.isCancellable();
	}

	public EventBus registerListener(Object object)
	{
		if(!hasListener(object))
		{
			Method[] methods = object.getClass().getDeclaredMethods();
			for(Method method : methods)
			{
				if(method.isAnnotationPresent(EventSubscribe.class))
				{
					if(method.getParameterTypes().length > 1)
					{
						Log.error("Method "+method.getName()+" is declared as event listener but has more than one parameter");
						return this;
					}
					else if(method.getParameterTypes() == null || method.getParameterTypes().length == 0)
					{
						Log.error("Method "+method.getName()+" is declared as event listener but has no parameter");
						return this;
					}
					if(Event.class.isAssignableFrom(method.getParameterTypes()[0]))
					{
						@SuppressWarnings("unchecked")
						Class<? extends Event> eventClass = (Class<? extends Event>)method.getParameterTypes()[0];
						EventListener listener = new EventListener(object, method.getName());
						ArrayList<EventListener> list = listeners.get(eventClass);
						if(list == null)
							list = new ArrayList<EventListener>();
						list.add(listener);
						listeners.put(eventClass, list);
						Log.message("Registred "+object.getClass().getName()+" to "+eventClass.getName()+" with "+method.getName()); // TODO: remove
					}
					else
					{
						Log.error("Method "+method.getName()+" is declared as event listener but the parameter of type "+method.getParameterTypes()[0].getName()+" can't be cast to an Event instance");
						return this;
					}
				}
			}
		}
		return this;
	}

	public boolean hasListener(Object object)
	{
		Iterator<ArrayList<EventListener>> it = getListeners().values().iterator();
		while(it.hasNext())
		{
			ArrayList<EventListener> list = it.next();
			if(list.contains(object))
				return true;
		}
		return false;
	}
}
