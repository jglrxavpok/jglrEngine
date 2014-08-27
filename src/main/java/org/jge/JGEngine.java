package org.jge;

import java.io.File;

import org.jge.crash.CrashReport;
import org.jge.game.Game;

public class JGEngine
{

	private static Game	 game;
	private static File	 folder;
	private static Disposer disposer = new Disposer();
	private static File	 gamesFolder;

	public static Game getGame()
	{
		return game;
	}

	public static void setGame(Game g)
	{
		game = g;
	}

	public static File getEngineFolder()
	{
		if(folder == null)
		{
			String appdata = System.getenv("APPDATA");
			if(appdata != null)
				folder = new File(appdata, ".jglrEngine");
			else
				folder = new File(System.getProperty("user.home"), ".jglrEngine");

			if(!folder.exists()) folder.mkdirs();
		}
		return folder;
	}

	public static void disposeAll()
	{
		disposer.disposeAll();
	}

	public static Disposer getDisposer()
	{
		return disposer;
	}

	public static ResourceLoader getResourceLoader()
	{
		return game.getClasspathResourceLoader();
	}

	public static String getEngineVersion()
	{
		return "GradleField:EngineVersion";
	}

	public static ResourceLoader getDiskResourceLoader()
	{
		return game.getDiskResourceLoader();
	}

	public static void crash(CrashReport report)
	{
		report.printStack();
		CoreEngine.getCurrent().kill();
		System.exit(-1);
	}

	public static File getGamesFolder()
	{
		if(gamesFolder == null)
		{
			gamesFolder = new File(getEngineFolder(), "Games");
			if(!gamesFolder.exists()) gamesFolder.mkdirs();
		}
		return gamesFolder;
	}
}
