package org.jge;

import static org.lwjgl.opengl.GL11.*;

import org.jge.components.Camera;
import org.jge.game.DummySceneObject;
import org.jge.game.Game;
import org.jge.maths.Matrix4;
import org.jge.maths.Transform;
import org.jge.render.RenderEngine;
import org.jge.render.Sprite;
import org.jge.render.Texture;

public abstract class LoadingScreen
{

	private Game			  game;
	private Sprite			backgroundImage;
	private Camera			camera;
	private LoadingScreenType type;

	public LoadingScreen(Game game, LoadingScreenType type)
	{
		this.type = type;
		this.game = game;
		try
		{
			this.backgroundImage = new Sprite(new Texture(CoreEngine.getCurrent().getClasspathResourceLoader().getResource(new ResourceLocation("textures", "loadingScreen.png"))));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		camera = new Camera(new Matrix4().initOrthographic(0, Window.getCurrent().getRealWidth(), 0, Window.getCurrent().getRealHeight(), -1.0, 100));
		new DummySceneObject(camera);
	}

	public abstract int runFirstTaskGroupAvailable();

	public abstract boolean isFinished();

	public abstract LoadingScreen convertTo(LoadingScreenType type);

	public abstract void addTaskGroup(LoadingTask... runnables);

	public void addTask(LoadingTask runnable)
	{
		addTaskGroup(runnable);
	}

	public LoadingScreen setBackgroundImage(Texture texture)
	{
		this.backgroundImage = new Sprite(texture);
		return this;
	}

	protected Game getGameInstance()
	{
		return game;
	}

	public void refreshScreen()
	{
		game.getWindow().bindAsRenderTarget();
		glClearColor(0, 0, 0, 0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		CoreEngine.getCurrent().getRenderEngine().disableGLCap(GL_CULL_FACE);
		render(backgroundImage, CoreEngine.getCurrent().getRenderEngine(), camera);
		CoreEngine.getCurrent().getRenderEngine().enableGLCap(GL_CULL_FACE);
		game.getWindow().refresh();
	}

	public void render(Sprite backgroundImage, RenderEngine engine, Camera camera)
	{
		backgroundImage.setWidth(Window.getCurrent().getRealWidth());
		backgroundImage.setHeight(Window.getCurrent().getRealHeight());
		backgroundImage.render(engine.defaultShader, Transform.NULL, camera, 1, engine);
	}

	public LoadingScreenType getType()
	{
		return type;
	}
}
