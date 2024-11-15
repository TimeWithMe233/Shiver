package Shiver.util.animation.glowesp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class GlowESPShaderUtil {
    private final int programID;
    private static final Minecraft mc = Minecraft.getMinecraft();

    public GlowESPShaderUtil(String fragmentShaderLoc, String vertexShaderLoc) {
        int program = GL20.glCreateProgram();
        try {
            int fragmentShaderID;
            String roundedRectGradient = "#version 120\n\nuniform vec2 location, rectSize;\nuniform vec4 color1, color2, color3, color4;\nuniform float radius;\n\n#define NOISE .5/255.0\n\nfloat roundSDF(vec2 p, vec2 b, float r) {\n    return length(max(abs(p) - b , 0.0)) - r;\n}\n\nvec3 createGradient(vec2 coords, vec3 color1, vec3 color2, vec3 color3, vec3 color4){\n    vec3 color = mix(mix(color1.rgb, color2.rgb, coords.y), mix(color3.rgb, color4.rgb, coords.y), coords.x);\n    //Dithering the color\n    // from https://shader-tutorial.dev/advanced/color-banding-dithering/\n    color += mix(NOISE, -NOISE, fract(sin(dot(coords.xy, vec2(12.9898, 78.233))) * 43758.5453));\n    return color;\n}\n\nvoid main() {\n    vec2 st = gl_TexCoord[0].st;\n    vec2 halfSize = rectSize * .5;\n    \n    float smoothedAlpha =  (1.0-smoothstep(0.0, 2., roundSDF(halfSize - (gl_TexCoord[0].st * rectSize), halfSize - radius - 1., radius))) * color1.a;\n    gl_FragColor = vec4(createGradient(st, color1.rgb, color2.rgb, color3.rgb, color4.rgb), smoothedAlpha);\n}";
            String roundedRect = "#version 120\n\nuniform vec2 location, rectSize;\nuniform vec4 color;\nuniform float radius;\nuniform bool blur;\n\nfloat roundSDF(vec2 p, vec2 b, float r) {\n    return length(max(abs(p) - b, 0.0)) - r;\n}\n\n\nvoid main() {\n    vec2 rectHalf = rectSize * .5;\n    // Smooth the result (free antialiasing).\n    float smoothedAlpha =  (1.0-smoothstep(0.0, 1.0, roundSDF(rectHalf - (gl_TexCoord[0].st * rectSize), rectHalf - radius - 1., radius))) * color.a;\n    gl_FragColor = vec4(color.rgb, smoothedAlpha);// mix(quadColor, shadowColor, 0.0);\n\n}";
            switch (fragmentShaderLoc) {
                case "roundedRect": {
                    fragmentShaderID = this.createShader(new ByteArrayInputStream(roundedRect.getBytes()), 35632);
                    break;
                }
                case "roundedRectGradient": {
                    fragmentShaderID = this.createShader(new ByteArrayInputStream(roundedRectGradient.getBytes()), 35632);
                    break;
                }
                default: {
                    fragmentShaderID = this.createShader(mc.getResourceManager().getResource(new ResourceLocation(fragmentShaderLoc)).getInputStream(), 35632);
                }
            }
            GL20.glAttachShader((int)program, (int)fragmentShaderID);
            int vertexShaderID = this.createShader(mc.getResourceManager().getResource(new ResourceLocation(vertexShaderLoc)).getInputStream(), 35633);
            GL20.glAttachShader((int)program, (int)vertexShaderID);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        GL20.glLinkProgram((int)program);
        int status = GL20.glGetProgrami((int)program, (int)35714);
        if (status == 0) {
            throw new IllegalStateException("Shader failed to link!");
        }
        this.programID = program;
    }

    public GlowESPShaderUtil(String fragmentShaderLoc) {
        this(fragmentShaderLoc, "acrimony/shader/glowesp/vertex.vsh");
    }

    public static void drawQuads() {
        ScaledResolution sr = new ScaledResolution(mc);
        float width = (float)sr.getScaledWidth_double();
        float height = (float)sr.getScaledHeight_double();
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex2f((float)0.0f, (float)0.0f);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex2f((float)0.0f, (float)height);
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex2f((float)width, (float)height);
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex2f((float)width, (float)0.0f);
        GL11.glEnd();
    }

    public void init() {
        GL20.glUseProgram((int)this.programID);
    }

    public void unload() {
        GL20.glUseProgram((int)0);
    }

    public int getUniform(String name) {
        return GL20.glGetUniformLocation((int)this.programID, (CharSequence)name);
    }

    public void setUniformf(String name, float ... args) {
        int loc = GL20.glGetUniformLocation((int)this.programID, (CharSequence)name);
        switch (args.length) {
            case 1: {
                GL20.glUniform1f((int)loc, (float)args[0]);
                break;
            }
            case 2: {
                GL20.glUniform2f((int)loc, (float)args[0], (float)args[1]);
                break;
            }
            case 3: {
                GL20.glUniform3f((int)loc, (float)args[0], (float)args[1], (float)args[2]);
                break;
            }
            case 4: {
                GL20.glUniform4f((int)loc, (float)args[0], (float)args[1], (float)args[2], (float)args[3]);
            }
        }
    }

    public void setUniformi(String name, int ... args) {
        int loc = GL20.glGetUniformLocation((int)this.programID, (CharSequence)name);
        if (args.length > 1) {
            GL20.glUniform2i((int)loc, (int)args[0], (int)args[1]);
        } else {
            GL20.glUniform1i((int)loc, (int)args[0]);
        }
    }

    private int createShader(InputStream inputStream, int shaderType) {
        int shader = GL20.glCreateShader((int)shaderType);
        GL20.glShaderSource((int)shader, (CharSequence)GlowESPFIleUtils.readInputStream(inputStream));
        GL20.glCompileShader((int)shader);
        if (GL20.glGetShaderi((int)shader, (int)35713) == 0) {
            System.out.println(GL20.glGetShaderInfoLog((int)shader, (int)4096));
            throw new IllegalStateException(String.format("Shader (%s) failed to compile!", shaderType));
        }
        return shader;
    }
}

