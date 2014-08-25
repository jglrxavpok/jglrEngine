package org.jge.render.shaders;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.jge.ResourceLocation;
import org.jge.RuntimeEngineException;
import org.jge.VirtualResourceLoader;
import org.jge.gpuresources.ShaderResource;
import org.jge.maths.Matrix4;
import org.jge.maths.Quaternion;
import org.jge.maths.Vector2;
import org.jge.maths.Vector3;
import org.jglrxavpok.jlsl.BytecodeDecoder;
import org.jglrxavpok.jlsl.JLSLContext;
import org.jglrxavpok.jlsl.glsl.FragmentShader;
import org.jglrxavpok.jlsl.glsl.GLSLEncoder;
import org.jglrxavpok.jlsl.glsl.ShaderBase;
import org.jglrxavpok.jlsl.glsl.VertexShader;

public class JavaShader extends Shader
{

	public static final boolean DEBUG_PRINT_GLSL_TRANSLATION = true;
	
	public JavaShader(int glslversion, Class<? extends ShaderBase>... shaderClasses)
	{
		VirtualResourceLoader resLoader = new VirtualResourceLoader();
		
		GLSLEncoder encoder = new GLSLEncoder(glslversion);
		GLSLEncoder.DEBUG = false;
		BytecodeDecoder.DEBUG = false;
		encoder.setGLSLTranslation(Matrix4.class.getCanonicalName(), "mat4");
		encoder.setGLSLTranslation(Vector2.class.getCanonicalName(), "vec2");
		encoder.setGLSLTranslation(Vector3.class.getCanonicalName(), "vec3");
		encoder.setGLSLTranslation(Quaternion.class.getCanonicalName(), "vec4");
		
		JLSLContext context = new JLSLContext(new BytecodeDecoder(), encoder);
		String name = null;
		for(Class<? extends ShaderBase> shaderClass : shaderClasses)
		{
			String type = null;
			if(VertexShader.class.isAssignableFrom(shaderClass) || JGEVertexShader.class.isAssignableFrom(shaderClass))
			{
				type = ".vs";
			}
			else if(FragmentShader.class.isAssignableFrom(shaderClass) || JGEFragmentShader.class.isAssignableFrom(shaderClass))
			{
				type = ".fs";
			}	
			
			if(type != null && shaderClass != JGESimpleVertexShader.class)
			{
				name = shaderClass.getSimpleName();
			}
				
			StringWriter sout = new StringWriter();
			PrintWriter out = new PrintWriter(sout);
			context.execute(shaderClass, out);
			if(DEBUG_PRINT_GLSL_TRANSLATION)
				System.out.println(sout.getBuffer().toString());
			if(type != null)
				resLoader.addResource(type, sout.getBuffer().toString().getBytes());
		}
		
		setResource(new ShaderResource().bindName(name));
		ResourceLocation partialPath = new ResourceLocation("");
        ResourceLocation vertLoc = new ResourceLocation(partialPath.getFullPath()+".vs");
        ResourceLocation geomLoc = new ResourceLocation(partialPath.getFullPath()+".gs");
        ResourceLocation fragLoc = new ResourceLocation(partialPath.getFullPath()+".fs");
        try
        {
            if(resLoader.doesResourceExists(vertLoc))
            {
            	addVertexShader(resLoader.getResource(vertLoc));
            }
            
            if(resLoader.doesResourceExists(geomLoc))
            {
            	addGeometryShader(resLoader.getResource(geomLoc));
            }
            
            if(resLoader.doesResourceExists(fragLoc))
            {
            	addFragmentShader(resLoader.getResource(fragLoc));
            }
        }
        catch(Exception e)
        {
        	throw new RuntimeEngineException(e);
        }
        compileShader();
    
    	loadAllUniforms();
	}

}
