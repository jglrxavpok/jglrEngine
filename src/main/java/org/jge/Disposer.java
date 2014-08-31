package org.jge;

import java.util.ArrayList;

public class Disposer
{
	private ArrayList<Disposable> disposables = new ArrayList<Disposable>();

	public void disposeAll()
	{
		disposables.forEach(Disposable::dispose);
		disposables.clear();
	}

	public void add(Disposable d)
	{
		disposables.add(d);
	}

	public void dispose(Disposable d)
	{
		d.dispose();
		disposables.remove(d);
	}

}
