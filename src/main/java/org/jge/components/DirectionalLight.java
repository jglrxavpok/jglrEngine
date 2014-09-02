package org.jge.components;

import org.jge.ResourceLocation;
import org.jge.maths.Maths;
import org.jge.maths.Matrix4;
import org.jge.maths.Vector3;
import org.jge.render.shaders.Shader;

public class DirectionalLight extends BaseLight
{

	private float halfArea;

	public DirectionalLight()
	{
		this(Vector3.get(1, 1, 1), 1f, 200);
	}

	public DirectionalLight(Vector3 color, float intensity)
	{
		this(color, intensity, 200);
	}

	public DirectionalLight(Vector3 color, float intensity, float shadowArea)
	{
		super(color, intensity);

		setShader(new Shader(new ResourceLocation("shaders", "forward-directional")));

		halfArea = shadowArea / 2.0f;
		setShadowingInfo(new ShadowingInfo(new Matrix4().initOrthographic(-halfArea, halfArea, -halfArea, halfArea, -halfArea, halfArea)).flipFaces(true));
	}

	public LightCamTrans getLightCamTrans(Camera mainCam)
	{
		LightCamTrans result = new LightCamTrans();
		result.pos = mainCam.getPos().add(mainCam.getForward().mul(halfArea));
		result.rot = getParent().getTransform().getTransformedRotation();

		double worldTexelSize = halfArea * 2.0 / (double)getShadowingInfo().getShadowMapSize().getSize();

		Vector3 lightCamPos = result.pos.rotate(result.rot.conjugate()).copy();

		lightCamPos.setX((float)(Maths.floor(lightCamPos.getX() / worldTexelSize) * worldTexelSize));
		lightCamPos.setY((float)(Maths.floor(lightCamPos.getY() / worldTexelSize) * worldTexelSize));

		result.pos = lightCamPos.rotate(result.rot);
		return result;
	}

	public Vector3 getDirection()
	{
		return getParent().getTransform().getTransformedRotation().getForward();
	}

}
