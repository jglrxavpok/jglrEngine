package org.jge;

import java.util.LinkedList;

import org.jge.game.Game;

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
 * 
 * @author jglrxavpok
 * 
 */
public class MonoThreadedLoadingScreen extends LoadingScreen
{

	private LinkedList<LoadingTask> tasksList = new LinkedList<LoadingTask>();

	public MonoThreadedLoadingScreen(Game game)
	{
		super(game, LoadingScreenType.MONO_THREADED);
	}

	public LoadingScreen convertTo(LoadingScreenType type)
	{
		if(type == LoadingScreenType.MONO_THREADED)
			return this;
		else if(type == LoadingScreenType.MULTI_THREADED)
		{
			MultiThreadedLoadingScreen multiThreaded = new MultiThreadedLoadingScreen(getGameInstance());
			for(LoadingTask task : tasksList)
				multiThreaded.addTask(task);
			return multiThreaded;
		}
		return null;
	}

	public int runFirstTaskGroupAvailable()
	{
		if(tasksList.isEmpty()) return 0;
		if(!tasksList.get(0).run()) return -1;
		tasksList.remove(0);
		return tasksList.size();
	}

	@Override
	public void addTaskGroup(LoadingTask... runnables)
	{
		for(LoadingTask r : runnables)
			tasksList.add(r);
	}

	@Override
	public boolean isFinished()
	{
		return tasksList.isEmpty();
	}

}
