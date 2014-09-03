package org.jge.tests;

import java.nio.FloatBuffer;
import java.util.Properties;

import org.jge.CoreEngine;
import org.jge.EngineException;
import org.jge.LoadingTask;
import org.jge.MultiThreadedLoadingScreen;
import org.jge.ResourceLocation;
import org.jge.Window;
import org.jge.ZipSimpleResourceLoader;
import org.jge.cl.GPUProgram;
import org.jge.cl.GPUProgramObject;
import org.jge.components.Camera;
import org.jge.components.DirectionalLight;
import org.jge.components.FreeLook;
import org.jge.components.FreeMove;
import org.jge.components.MeshRenderer;
import org.jge.components.SceneObject;
import org.jge.components.ShadowMapSize;
import org.jge.components.SpotLight;
import org.jge.components.hud.HUDSpriteObject;
import org.jge.components.hud.TextLabelObject;
import org.jge.crash.CrashReport;
import org.jge.game.Game;
import org.jge.game.Input;
import org.jge.maths.Maths;
import org.jge.maths.Quaternion;
import org.jge.maths.Vector3;
import org.jge.phys.PhysicsComponent;
import org.jge.phys.PhysicsShape;
import org.jge.phys.shapes.BoxPhysShape;
import org.jge.phys.shapes.MeshPhysShape;
import org.jge.render.Animation;
import org.jge.render.Material;
import org.jge.render.ParticleGenerator;
import org.jge.render.Sprite;
import org.jge.render.Texture;
import org.jge.render.TextureAtlas;
import org.jge.render.TextureIcon;
import org.jge.render.TextureMap;
import org.jge.render.TextureRegion;
import org.jge.render.fonts.Font;
import org.jge.render.fonts.SimpleFont;
import org.jge.render.mesh.Mesh;
import org.jge.render.shaders.JGESimpleVertexShader;
import org.jge.render.shaders.JavaShader;
import org.jge.render.shaders.filters.FilterInvertFragmentShader;
import org.jge.script.Script;
import org.jge.script.lua.LuaScript;
import org.jge.sound.Music;
import org.jge.util.BinaryUtils;
import org.jge.util.Buffers;
import org.jge.util.Log;

/**
 * 
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
 */
public class TestGame extends Game
{

	public static void main(String[] args)
	{
		try
		{
			new CoreEngine(new TestGame()).start(new Window(960, 600), args);
		}
		catch(EngineException e)
		{
			e.printStackTrace();
		}
	}

	private Material	material;
	private SceneObject spotLightObject;
	private SpotLight   spotLight;
	private SceneObject cameraObject;
	private Mesh		lowMonkeyMesh;
	private Font		font;
	private TestMenu	menu;

	public void init()
	{
		try
		{
			TextureMap map = new TextureMap(getClasspathResourceLoader(), new ResourceLocation("test/textures", "icons"), true);

			map.fixSize(16, 16);
			map.generateIcon("../bricks.png");
			map.generateIcon("alchemist.png");
			map.generateIcon("archer.png");
			map.generateIcon("barbarian.png");
			map.generateIcon("block.png");
			map.generateIcon("enchanter.png");
			map.generateIcon("enchanter2.png");
			map.generateIcon("bomber.png");
			map.generateIcon("farmer.png");
			map.generateIcon("locked.png");
			map.generateIcon("enderman.png");
			map.generateIcon("sheep.png");
			map.generateIcon("../icon32.png");
			TextureIcon icon = map.generateIcon("spy.png");

			map.compile();
			Script script = new LuaScript(getClasspathResourceLoader().getResource(new ResourceLocation("text", "test.lua")));
			Log.message(script.run().toString());
			ZipSimpleResourceLoader loader = new ZipSimpleResourceLoader(getDiskResourceLoader().getResource(new ResourceLocation("./test.zip")));
			Log.error(BinaryUtils.toString(loader.getResource(new ResourceLocation("test/test.txt")).getData()));
			font = SimpleFont.instance;
			// getRenderEngine().addPostProcessingFilter(new Shader(new
			// ResourceLocation("shaders", "filter-texwaves")));
			getRenderEngine().addPostProcessingFilter(new JavaShader(130, JGESimpleVertexShader.class, FilterInvertFragmentShader.class));
			// getRenderEngine().addPostProcessingFilter(new Shader(new
			// ResourceLocation("shaders", "filter-oldtv")));
			menu = new TestMenu();
			Sprite sprite = new Sprite(new Texture(getClasspathResourceLoader().getResource(new ResourceLocation("test/textures", "heart.png")), Texture.FILTER_NEAREST)).setWidth(32).setHeight(32);
			SceneObject spriteObject = new HUDSpriteObject(sprite);
			spriteObject.getTransform().setPosition(Vector3.get(50, 50, 0));
			addToHUD(new TextLabelObject(font, "Hello t§uhere! How are you doing §cFFFF0000today, §r§bhm?", 0xFFFFFFFF));
			addToHUD(menu);
			addToHUD(new HUDSpriteObject(new Sprite(new TextureRegion(map.getTexture(), icon))));
			addToHUD(spriteObject);
			getRenderEngine().setAmbientColor(0.15f, 0.15f, 0.15f);
			material = new Material();
			material.setTexture("diffuse", new Texture(getClasspathResourceLoader().getResource(new ResourceLocation("test/textures", "bricks2.png"))));
			material.setTexture("normalMap", new Texture(getClasspathResourceLoader().getResource(new ResourceLocation("test/textures", "bricks2_normal.png"))));
			material.setTexture("dispMap", new Texture(getClasspathResourceLoader().getResource(new ResourceLocation("test/textures", "bricks2_disp.png"))));
			material.setFloat("specularIntensity", 0.5f);
			material.setFloat("specularPower", 4);
			getWindow().setIcons(getClasspathResourceLoader().getResource(new ResourceLocation("test/textures", "icon16.png")), getClasspathResourceLoader().getResource(new ResourceLocation("test/textures", "icon32.png")));
			Mesh mesh = new Mesh(getClasspathResourceLoader().getResource(new ResourceLocation("test/models/plane.obj")));

			Material materialBricks = new Material();
			try
			{
				materialBricks.setTexture("diffuse", new Texture(getClasspathResourceLoader().getResource(new ResourceLocation("test/textures", "bricks.png"))));
				materialBricks.setFloat("specularIntensity", 0.5f);
				materialBricks.setFloat("specularPower", 4);
				materialBricks.setTexture("normalMap", new Texture(getClasspathResourceLoader().getResource(new ResourceLocation("test/textures", "bricks_normal.png"))));
				materialBricks.setTexture("dispMap", new Texture(getClasspathResourceLoader().getResource(new ResourceLocation("test/textures", "bricks_disp.png"))));
			}
			catch(Exception e1)
			{
				e1.printStackTrace();
			}
			MeshRenderer renderer = new MeshRenderer(mesh, materialBricks);

			SceneObject plane = new SceneObject();
			plane.addComponent(renderer);
			addToWorld(plane);

			spotLightObject = new SceneObject();
			spotLight = new SpotLight(Vector3.get(1, 1, 1), 0.8f, Vector3.get(0, 0, 0.005f), (float)Maths.toRadians(90));
			spotLightObject.addComponent(spotLight);
			spotLight.getShadowingInfo().setShadowMapSize(ShadowMapSize._128x128);

			Mesh mesh1 = new Mesh(getClasspathResourceLoader().getResource(new ResourceLocation("test/models/plane.obj")));

			Mesh mesh2 = new Mesh(getClasspathResourceLoader().getResource(new ResourceLocation("test/models/plane.obj")));

			SceneObject test1 = new SceneObject().addComponent(new MeshRenderer(mesh1, material));
			SceneObject test2 = new SceneObject().addComponent(new MeshRenderer(mesh2, material));
			Camera camera = new Camera((float)Maths.toRadians(70), (float)Window.getCurrent().getWidth() / (float)Window.getCurrent().getHeight(), 0.1f, 400);
			cameraObject = new SceneObject().addComponent(camera).addComponent(new FreeLook(0.5, true)).addComponent(new FreeMove(0.5f));

			addToWorld(cameraObject);

			test1.getTransform().setPosition(Vector3.get(0, 5, 0));
			test1.getTransform().setRotation(new Quaternion(Vector3.get(0, 1, 0), 0.4f));
			test2.getTransform().setPosition(Vector3.get(0, 0, 2.5f * 10));
			test1.addChild(test2);

			test1.addComponentAs("physics", new PhysicsComponent(0, new BoxPhysShape(Vector3.get(8, 0.12f, 8))));
			test2.addComponentAs("physics", new PhysicsComponent(0, new BoxPhysShape(Vector3.get(8, 0.12f, 8))));

			addToWorld(test1);

			SceneObject directionalLight = new SceneObject().addComponent(new DirectionalLight(Vector3.get(1, 1, 1), 1f));
			directionalLight.getTransform().rotate(Vector3.get(1, 0, 0), (float)Maths.toRadians(-45)).rotate(Vector3.get(0, 1, 0), (float)Maths.toRadians(45));
			addToWorld(directionalLight);

			addToWorld(spotLightObject);
			try
			{
				lowMonkeyMesh = new Mesh((getClasspathResourceLoader().getResource(new ResourceLocation("test/models", "lowmonkey.obj"))));
				for(int i = 0; i < 2; i++ )
				{
					createMonkey();
				}

			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			createCube();
			plane.getTransform().scale(4, 4, 4);

			ParticleGenerator gen = new ParticleGenerator(1, 1);
			gen.getTransform().setPosition(Vector3.get(0, 5, 0));
			addToWorld(gen);

			GPUProgram program = new GPUProgram(getClasspathResourceLoader().getResource(new ResourceLocation("test/cl", "test.cls")));
			int size = 1000000;
			float[] aArray = new float[size];
			float[] bArray = new float[size];
			float[] result = new float[size];
			for(int i = 0; i < aArray.length; i++ )
			{
				aArray[i] = i * i;
				bArray[i] = i + i;
			}
			GPUProgramObject aMem = program.createMemory(aArray);
			GPUProgramObject bMem = program.createMemory(bArray);
			GPUProgramObject resultMem = program.createMemory(size * 4);

			long cpuTime = 0;
			long gpuTime = 0;
			int passes = 0;

			program.getData().runKernel("test", new GPUProgramObject[]
			{
					aMem, bMem, resultMem, new GPUProgramObject(size)
			}, size); // Fair play

			for(int index = 0; index < passes; index++ )
			{
				long s = System.currentTimeMillis();

				program.getData().runKernel("test", new GPUProgramObject[]
				{
						aMem, bMem, resultMem, new GPUProgramObject(size)
				}, size);

				long gpuTime1 = (System.currentTimeMillis() - s);
				Log.error("(" + index + ")GPU used " + gpuTime1 + " ms");

				s = System.currentTimeMillis();
				for(int i = 0; i < size; i++ )
				{
					result[i] = aArray[i] + bArray[i];
				}

				long cpuTime1 = (System.currentTimeMillis() - s);
				Log.error("(" + index + ")CPU used " + cpuTime1 + " ms");

				cpuTime += cpuTime1;
				gpuTime += gpuTime1;
			}
			Log.error("CPU average: " + ((float)cpuTime / (float)passes) + " to add two arrays of size " + size);
			Log.error("GPU average: " + ((float)gpuTime / (float)passes) + " to add two arrays of size " + size);
			FloatBuffer w = Buffers.createFloatBuffer(size);
			program.read(resultMem, w, true);

			PhysicsShape planeShape = new BoxPhysShape(Vector3.get(31.5f, 0.12f, 31.5f));
			plane.addComponentAs("physics", new PhysicsComponent(0, planeShape));

			camera.setName("world");

			Music dapperCadaver = Music.get(getClasspathResourceLoader().getResource(new ResourceLocation("test/music", "DapperCadaver-TF2.wav")));
			// dapperCadaver.play();
			dapperCadaver.setGain(0.01f);
			dapperCadaver.setPitch(2f);
			dapperCadaver.setLooping(true);

			getRenderEngine().setCamera(camera);
			getSoundEngine().setPlayerTransform(cameraObject.getTransform());
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
	}

	private void createTorus() throws Exception
	{
		SceneObject cube = new SceneObject();
		Mesh mesh = new Mesh((getClasspathResourceLoader().getResource(new ResourceLocation("test/models", "cone.obj"))));
		Material mat = new Material();
		mat.setTexture("diffuse", new Texture(getClasspathResourceLoader().getResource(new ResourceLocation("test/textures", "testDiffuseTorus.png"))));
		cube.addComponent(new MeshRenderer(mesh, mat));
		cube.getTransform().setPosition(cameraObject.getTransform().getTransformedPos());
		cube.getTransform().rotate(Vector3.get(0, 1, 0), (float)Maths.toRadians(-30));
		addToWorld(cube);
		PhysicsShape cubeShape = new MeshPhysShape(mesh);
		cube.addComponentAs("physics", new PhysicsComponent(1, cubeShape));

	}

	private void createCube() throws Exception
	{
		SceneObject cube = new SceneObject();
		Material mat = new Material();
		mat.setTexture("diffuse", new Texture(getClasspathResourceLoader().getResource(new ResourceLocation("test/textures", "testDiffuseTorus.png"))));
		cube.addComponent(new MeshRenderer(new Mesh((getClasspathResourceLoader().getResource(new ResourceLocation("test/models", "cube.obj")))), material));
		cube.getTransform().setPosition(cameraObject.getTransform().getTransformedPos());
		cube.getTransform().rotate(Vector3.get(0, 1, 0), (float)Maths.toRadians(-30));
		addToWorld(cube);
		cube.getTransform().scale(2, 2, 2);
		PhysicsShape cubeShape = new BoxPhysShape(Vector3.get(2, 2, 2));
		cube.addComponentAs("physics", new PhysicsComponent(1, cubeShape));
		// cube.addComponent(new SoundSource(new
		// Sound(getClasspathResourceLoader().getResource(new
		// ResourceLocation("test/sounds", "Jump1.wav"))), 1000));
	}

	public void updateGame(double delta)
	{
		if(Input.isKeyJustPressed(Input.KEY_C) && Input.isKeyDown(Input.KEY_LCONTROL))
		{
			crash(new CrashReport("Unknown"));
		}

		if(Input.isKeyJustPressed(Input.KEY_G))
		{
			MultiThreadedLoadingScreen loadingScreen = new MultiThreadedLoadingScreen(this);
			try
			{
				Texture t = new Texture(getClasspathResourceLoader().getResource(new ResourceLocation("test/textures/ascensionPlayer.png")), Texture.FILTER_NEAREST);
				Animation anim = Animation.fromAtlas(new TextureAtlas(t, 16, 16), 0.15, true);
				loadingScreen.setAnimation(anim);
			}
			catch(Exception e1)
			{
				e1.printStackTrace();
			}
			loadingScreen.addTask(new LoadingTask()
			{
				@Override
				public boolean run()
				{
					try
					{
						Thread.sleep(2000);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					return true;
				}
			});
			this.setLoadingScreen(loadingScreen);
		}
		if(Input.isKeyDown(Input.KEY_ESCAPE))
		{
			this.stop();
		}
		if(Input.isKeyDown(Input.KEY_F))
		{
			spotLightObject.getTransform().setPosition(cameraObject.getTransform().getTransformedPos());
			spotLightObject.getTransform().setRotation(cameraObject.getTransform().getTransformedRotation());
		}

		if(Input.isKeyJustPressed(Input.KEY_C))
		{
			int r = (int)Maths.floor(Maths.rand() * 3);
			if(r == 0)
			{
				try
				{
					createCube();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			else if(r == 1)
			{
				try
				{
					createMonkey();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				try
				{
					createTorus();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private void createMonkey() throws Exception
	{
		SceneObject monkey = new SceneObject();
		Mesh monkeyMesh = new Mesh((getClasspathResourceLoader().getResource(new ResourceLocation("test/models/monkey.obj"))));
		Material material2 = new Material();
		material2.setTexture("diffuse", new Texture(getClasspathResourceLoader().getResource(new ResourceLocation("test/textures", "bricks.png"))));
		material2.setTexture("normalMap", new Texture(getClasspathResourceLoader().getResource(new ResourceLocation("test/textures", "bricks_normal.png"))));
		material2.setTexture("dispMap", getRenderEngine().loadTexture(getClasspathResourceLoader().getResource(new ResourceLocation("test/textures", "bricks_disp.png"))));
		material2.setFloat("specularIntensity", 0.5f);
		material2.setFloat("specularPower", 4);
		monkey.addComponent(new MeshRenderer(monkeyMesh, material2));

		monkey.getTransform().setPosition(cameraObject.getTransform().getTransformedPos());
		monkey.getTransform().setRotation(cameraObject.getTransform().getTransformedRotation());

		PhysicsShape monkeyShape = new MeshPhysShape(lowMonkeyMesh);
		monkey.addComponentAs("physics", new PhysicsComponent(1, monkeyShape));

		Camera viewCamera = new Camera((float)Maths.toRadians(150), (float)Window.getCurrent().getPhysicalWidth() / (float)Window.getCurrent().getPhysicalHeight(), 0.0001f, 1000);
		monkey.addComponent(viewCamera);
		viewCamera.setName("monkey");

		menu.setViewRoot(getSceneRoot().getChild("world"));
		menu.setViewCam(viewCamera);
		addToWorld(monkey);
	}

	@Override
	public String getGameName()
	{
		return "Test Game";
	}

	public void onPropertiesLoad(Properties loaded)
	{
		Log.error("test is " + loaded.getProperty("test"));
	}

}
