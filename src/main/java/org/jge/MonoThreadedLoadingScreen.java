package org.jge;

import java.util.LinkedList;

import org.jge.game.Game;

public class MonoThreadedLoadingScreen extends LoadingScreen
{

	private LinkedList<LoadingTask> tasksList = new  LinkedList<LoadingTask>();

	public MonoThreadedLoadingScreen(Game game)
	{
		super(game);
	}
	
	public LoadingScreen convertTo(LoadingScreenType type)
	{
		if(type == LoadingScreenType.MONO_THREADED)
			return this;
		else if(type == LoadingScreenType.MULTI_THREADED)
			return null;
		return null;
	}
	
	public int runFirstTaskGroupAvailable()
	{
		if(tasksList.isEmpty())
			return 0;
		if(!tasksList.get(0).run())
			return -1;
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
