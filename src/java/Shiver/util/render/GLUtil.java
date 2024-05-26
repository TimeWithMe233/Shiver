package Shiver.util.render;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class GLUtil {
    public static int[] enabledCaps = new int[32];

    public static void enableDepth() {
        GlStateManager.enableDepth();
        GlStateManager.depthMask((boolean)true);
    }

    public static void disableDepth() {
        GlStateManager.disableDepth();
        GlStateManager.depthMask((boolean)false);
    }

    public static void enableCaps(int ... caps) {
        for (int cap : caps) {
            GL11.glEnable((int)cap);
        }
        enabledCaps = caps;
    }

    public static void disableCaps() {
        for (int cap : enabledCaps) {
            GL11.glDisable((int)cap);
        }
    }

    public static void startBlend() {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((int)770, (int)771);
    }

    public static void endBlend() {
        GlStateManager.disableBlend();
    }

    public static void setup2DRendering(boolean blend) {
        if (blend) {
            GLUtil.startBlend();
        }
        GlStateManager.disableTexture2D();
    }

    public static void setup2DRendering() {
        GLUtil.setup2DRendering(true);
    }

    public static void end2DRendering() {
        GlStateManager.enableTexture2D();
        GLUtil.endBlend();
    }

    public static void startRotate(float x, float y, float rotate) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)0.0f);
        GlStateManager.rotate((float)rotate, (float)0.0f, (float)0.0f, (float)-1.0f);
        GlStateManager.translate((float)(-x), (float)(-y), (float)0.0f);
    }

    public static void endRotate() {
        GlStateManager.popMatrix();
    }
}

