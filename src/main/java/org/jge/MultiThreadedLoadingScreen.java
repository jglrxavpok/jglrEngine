package org.jge;

import java.util.ArrayList;
import java.util.Stack;

import org.jge.components.Camera;
import org.jge.game.Game;
import org.jge.maths.Transform;
import org.jge.maths.Vector3;
import org.jge.render.Animation;
import org.jge.render.RenderEngine;
import org.jge.render.Sprite;

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
public class MultiThreadedLoadingScreen extends LoadingScreen
{

	class ThreadedTaskGroup extends Thread
	{
		private LoadingTask[] tasks;
		private int		   index;

		public ThreadedTaskGroup(LoadingTask... tasks)
		{
			super("Thread tasks group, size=" + tasks.length);
			this.tasks = tasks;
		}

		public int getCurrentIndex()
		{
			return index;
		}

		public void run()
		{
			index = 0;
			for(; index < tasks.length; index++ )
			{
				LoadingTask task = tasks[index];
				if(!task.run())
				{
					failed.add(this);
					break;
				}
			}
			threads.remove(this);
		}
	}

	private ArrayList<ThreadedTaskGroup> failed	 = new ArrayList<MultiThreadedLoadingScreen.ThreadedTaskGroup>();
	private ArrayList<ThreadedTaskGroup> threads	= new ArrayList<MultiThreadedLoadingScreen.ThreadedTaskGroup>();
	private Stack<LoadingTask[]>		 taskGroups = new Stack<LoadingTask[]>();
	private Animation					animation;

	public MultiThreadedLoadingScreen(Game game)
	{
		super(game, LoadingScreenType.MULTI_THREADED);
	}

	@Override
	public int runFirstTaskGroupAvailable()
	{
		if(!taskGroups.isEmpty())
		{
			ThreadedTaskGroup taskGroup = new ThreadedTaskGroup(taskGroups.pop());
			threads.add(taskGroup);
			taskGroup.start();
		}
		return taskGroups.size();
	}

	@Override
	public boolean isFinished()
	{
		return taskGroups.isEmpty() && threads.isEmpty();
	}

	@Override
	public LoadingScreen convertTo(LoadingScreenType type)
	{
		if(type == LoadingScreenType.MULTI_THREADED)
		{
			return this;
		}
		else if(type == LoadingScreenType.MONO_THREADED)
		{
			MonoThreadedLoadingScreen monoThreaded = new MonoThreadedLoadingScreen(getGameInstance());
			for(LoadingTask[] taskGroup : taskGroups)
				monoThreaded.addTaskGroup(taskGroup);
			return monoThreaded;
		}
		return null;
	}

	@Override
	public void addTaskGroup(LoadingTask... runnables)
	{
		taskGroups.push(runnables);
	}

	public void setAnimation(Animation anim)
	{
		this.animation = anim;
	}

	public void render(Sprite backgroundImage, RenderEngine engine, Camera camera)
	{
		super.render(backgroundImage, engine, camera);
		if(animation != null)
		{
			int tick = CoreEngine.getCurrent().getTick();
			float x = (float)(backgroundImage.getWidth() - animation.getSpriteWidth());
			float y = 0;
			Transform.GLOBAL.setPosition(Vector3.get(x, y, 0));
			animation.render(engine.defaultShader, Transform.GLOBAL, camera, 1, engine, tick);
			Transform.GLOBAL.setPosition(Vector3.NULL);
		}
	}
}
