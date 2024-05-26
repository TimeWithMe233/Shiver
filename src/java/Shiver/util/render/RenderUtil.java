package Shiver.util.render;

import Shiver.util.IMinecraft;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class RenderUtil {
    public static Frustum frustrum = new Frustum();

    public static void setColor(int color) {
        float a = (float)(color >> 24 & 0xFF) / 255.0f;
        float r = (float)(color >> 16 & 0xFF) / 255.0f;
        float g = (float)(color >> 8 & 0xFF) / 255.0f;
        float b = (float)(color & 0xFF) / 255.0f;
        GL11.glColor4f((float)r, (float)g, (float)b, (float)a);
    }

    public static void setAlphaLimit(float limit) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc((int)516, (float)((float)((double)limit * 0.01)));
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return RenderUtil.isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    private static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }

    public static void drawLoadingCircleFast(float x, float y, Color color) {
        for (int i = 0; i < 2; ++i) {
            int rot = (int)(System.nanoTime() / 1200000L * (long)i % 360L);
            RenderUtil.drawCircle(x, y, (float)i * 7.0f, rot - 180, rot, color);
        }
    }

    public static void drawLoadingCircleNormal(float x, float y, Color color) {
        for (int i = 0; i < 2; ++i) {
            int rot = (int)(System.nanoTime() / 1200000L * (long)i % 360L);
            RenderUtil.drawCircle(x, y, (float)i * 7.0f, rot - 180, rot, color);
        }
    }

    public static void drawLoadingCircleSlow(float x, float y, Color color) {
        for (int i = 0; i < 2; ++i) {
            int rot = (int)(System.nanoTime() / 8200000L * (long)i % 360L);
            RenderUtil.drawCircle(x, y, (float)i * 7.0f, rot - 180, rot, color);
        }
    }

    public static void drawCircle(float x, float y, float radius, int start, int end, Color color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        RenderUtil.glColor(color.getRGB());
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)2.5f);
        GL11.glBegin((int)3);
        for (float i = (float)end; i >= (float)start; i -= 4.0f) {
            GL11.glVertex2f((float)((float)((double)x + Math.cos(i * MathHelper.PI / 180.0f) * (double)(radius * 1.001f))), (float)((float)((double)y + Math.sin(i * MathHelper.PI / 180.0f) * (double)(radius * 1.001f))));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void prepareBoxRender(float lineWidth, double red, double green, double blue, double alpha) {
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)lineWidth);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)2848);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
    }

    public static void drawFilledCircleNoGL(int x, int y, double r, int c, int quality) {
        RenderUtil.resetColor();
        RenderUtil.setAlphaLimit(0.0f);
        GLUtil.setup2DRendering();
        RenderUtil.color(c);
        GL11.glBegin((int)6);
        for (int i = 0; i <= 360 / quality; ++i) {
            double x2 = Math.sin((double)(i * quality) * Math.PI / 180.0) * r;
            double y2 = Math.cos((double)(i * quality) * Math.PI / 180.0) * r;
            GL11.glVertex2d((double)((double)x + x2), (double)((double)y + y2));
        }
        GL11.glEnd();
        GLUtil.end2DRendering();
    }

    public static void renderBlockBox(RenderManager rm, float partialTicks, double x, double y, double z) {
        AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        RenderGlobal.func_181561_a((AxisAlignedBB)new AxisAlignedBB(bb.minX - x + (x - rm.renderPosX), bb.minY - y + (y - rm.renderPosY), bb.minZ - z + (z - rm.renderPosZ), bb.maxX - x + (x - rm.renderPosX), bb.maxY - y + (y - rm.renderPosY), bb.maxZ - z + (z - rm.renderPosZ)));
    }

    public static void renderEntityBox(RenderManager rm, float partialTicks, Entity entity) {
        AxisAlignedBB bb = entity.getEntityBoundingBox();
        double posX = RenderUtil.interpolate(entity.posX, entity.lastTickPosX, partialTicks);
        double posY = RenderUtil.interpolate(entity.posY, entity.lastTickPosY, partialTicks);
        double posZ = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);
        RenderGlobal.func_181561_a((AxisAlignedBB)new AxisAlignedBB(bb.minX - 0.05 - entity.posX + (posX - rm.renderPosX), bb.minY - 0.05 - entity.posY + (posY - rm.renderPosY), bb.minZ - 0.05 - entity.posZ + (posZ - rm.renderPosZ), bb.maxX + 0.05 - entity.posX + (posX - rm.renderPosX), bb.maxY + 0.1 - entity.posY + (posY - rm.renderPosY), bb.maxZ + 0.05 - entity.posZ + (posZ - rm.renderPosZ)));
    }

    public static void renderCustomPlayerBox(RenderManager rm, float partialTicks, double x, double y, double z) {
        RenderUtil.renderCustomPlayerBox(rm, partialTicks, x, y, z, x, y, z);
    }

    public static void renderCustomPlayerBox(RenderManager rm, float partialTicks, double x, double y, double z, double lastX, double lastY, double lastZ) {
        AxisAlignedBB bb = new AxisAlignedBB(x - 0.3, y, z - 0.3, x + 0.3, y + 1.8, z + 0.3);
        double posX = RenderUtil.interpolate(x, lastX, partialTicks);
        double posY = RenderUtil.interpolate(y, lastY, partialTicks);
        double posZ = RenderUtil.interpolate(z, lastZ, partialTicks);
        RenderGlobal.func_181561_a((AxisAlignedBB)new AxisAlignedBB(bb.minX - 0.05 - x + (posX - rm.renderPosX), bb.minY - 0.05 - y + (posY - rm.renderPosY), bb.minZ - 0.05 - z + (posZ - rm.renderPosZ), bb.maxX + 0.05 - x + (posX - rm.renderPosX), bb.maxY + 0.1 - y + (posY - rm.renderPosY), bb.maxZ + 0.05 - z + (posZ - rm.renderPosZ)));
    }

    public static void stopBoxRender() {
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
    }

    public static void drawSoiledEntityESP(double x, double y, double z, double width, double height, int color) {
        GlStateManager.pushMatrix();
        RenderUtil.pre3D();
        IMinecraft.mc.entityRenderer.setupCameraTransform(IMinecraft.mc.timer.renderPartialTicks, 2);
        RenderUtil.glColor(color);
        RenderUtil.drawnlbBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        RenderUtil.post3D();
        GlStateManager.popMatrix();
    }

    public static void drawnlbBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void resetColor() {
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static double interpolate(double old, double now, float partialTicks) {
        return old + (now - old) * (double)partialTicks;
    }

    public static float interpolate(float old, float now, float partialTicks) {
        return old + (now - old) * partialTicks;
    }

    public static void color(int color, float alpha) {
        float r = (float)(color >> 16 & 0xFF) / 255.0f;
        float g = (float)(color >> 8 & 0xFF) / 255.0f;
        float b = (float)(color & 0xFF) / 255.0f;
        GlStateManager.color((float)r, (float)g, (float)b, (float)alpha);
    }

    public static void color(int color) {
        RenderUtil.color(color, (float)(color >> 24 & 0xFF) / 255.0f);
    }

    public static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void pre3D() {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDisable((int)2896);
        GL11.glDepthMask((boolean)false);
        GL11.glHint((int)3154, (int)4354);
    }

    public static void post3D() {
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }
}

