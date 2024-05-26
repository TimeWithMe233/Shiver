package Shiver.util.render;

import Shiver.util.IMinecraft;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class DrawUtil
implements IMinecraft {
    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(col1 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(col2 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glColor4f((float)f6, (float)f7, (float)f8, (float)f5);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }





    public static void renderTriangle(double startX, double startY, int color) {
        double endX = startX + 6.0;
        Gui.drawRect((double)startX, (double)startY, (double)endX, (double)(startY + 0.5), (int)color);
        Gui.drawRect((double)(startX + 0.5), (double)(startY + 0.5), (double)(endX - 0.5), (double)(startY + 1.0), (int)color);
        Gui.drawRect((double)(startX + 1.0), (double)(startY + 1.0), (double)(endX - 1.0), (double)(startY + 1.5), (int)color);
        Gui.drawRect((double)(startX + 1.5), (double)(startY + 1.5), (double)(endX - 1.5), (double)(startY + 2.0), (int)color);
        Gui.drawRect((double)(startX + 2.0), (double)(startY + 2.0), (double)(endX - 2.0), (double)(startY + 2.5), (int)color);
        Gui.drawRect((double)(startX + 2.5), (double)(startY + 2.5), (double)(endX - 2.5), (double)(startY + 3.0), (int)color);
    }

    public static void drawHead(ResourceLocation skin, int x, int y, int width, int height) {
        try {
            mc.getTextureManager().bindTexture(skin);
            GL11.glEnable((int)3042);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            Gui.drawScaledCustomSizeModalRect((int)x, (int)y, (float)8.0f, (float)8.0f, (int)8, (int)8, (int)width, (int)height, (float)64.0f, (float)64.0f);
            GL11.glDisable((int)3042);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
        try {
            mc.getTextureManager().bindTexture(image);
            GL11.glEnable((int)3042);
            Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
            GL11.glDisable((int)3042);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void drawRoundedRect(double x, double y, double x1, double y1, double radius, int color) {
        int i;
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        x *= 2.0;
        y *= 2.0;
        x1 *= 2.0;
        y1 *= 2.0;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        float a = (float)(color >> 24 & 0xFF) / 255.0f;
        float r = (float)(color >> 16 & 0xFF) / 255.0f;
        float g = (float)(color >> 8 & 0xFF) / 255.0f;
        float b = (float)(color & 0xFF) / 255.0f;
        GL11.glColor4f((float)r, (float)g, (float)b, (float)a);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)9);
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x + radius + Math.sin((double)i * Math.PI / 180.0) * radius * -1.0), (double)(y + radius + Math.cos((double)i * Math.PI / 180.0) * radius * -1.0));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)(x + radius + Math.sin((double)i * Math.PI / 180.0) * radius * -1.0), (double)(y1 - radius + Math.cos((double)i * Math.PI / 180.0) * radius * -1.0));
        }
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x1 - radius + Math.sin((double)i * Math.PI / 180.0) * radius), (double)(y1 - radius + Math.cos((double)i * Math.PI / 180.0) * radius));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)(x1 - radius + Math.sin((double)i * Math.PI / 180.0) * radius), (double)(y + radius + Math.cos((double)i * Math.PI / 180.0) * radius));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glPopAttrib();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawOutlinedRoundedRect(double x, double y, double width, double height, double radius, float linewidth, int color) {
        int i;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        double x1 = x + width;
        double y1 = y + height;
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        x *= 2.0;
        y *= 2.0;
        x1 *= 2.0;
        y1 *= 2.0;
        GL11.glLineWidth((float)linewidth);
        GL11.glDisable((int)3553);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)2);
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x + radius + Math.sin((double)i * Math.PI / 180.0) * (radius * -1.0)), (double)(y + radius + Math.cos((double)i * Math.PI / 180.0) * (radius * -1.0)));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)(x + radius + Math.sin((double)i * Math.PI / 180.0) * (radius * -1.0)), (double)(y1 - radius + Math.cos((double)i * Math.PI / 180.0) * (radius * -1.0)));
        }
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x1 - radius + Math.sin((double)i * Math.PI / 180.0) * radius), (double)(y1 - radius + Math.cos((double)i * Math.PI / 180.0) * radius));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)(x1 - radius + Math.sin((double)i * Math.PI / 180.0) * radius), (double)(y + radius + Math.cos((double)i * Math.PI / 180.0) * radius));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glPopAttrib();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void renderMainMenuBackground(GuiScreen screen, ScaledResolution sr) {
        int topColor = new Color(4, 2, 82).getRGB();
        int bottomColor = new Color(2, 0, 36).getRGB();
        screen.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), topColor, bottomColor);
    }

    public static enum DiagonalType {
        LEFT_TOP,
        RIGHT_TOP,
        LEFT_BOTTOM,
        RIGHT_BOTTOM;

    }
}

