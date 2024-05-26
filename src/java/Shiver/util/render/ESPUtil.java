package Shiver.util.render;

import Shiver.util.IMinecraft;
import Shiver.util.misc.MathUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class ESPUtil
implements IMinecraft {
    private static final Frustum frustum = new Frustum();
    private static final FloatBuffer windPos = BufferUtils.createFloatBuffer((int)4);
    private static final IntBuffer intBuffer = GLAllocation.createDirectIntBuffer((int)16);
    private static final FloatBuffer floatBuffer1 = GLAllocation.createDirectFloatBuffer((int)16);
    private static final FloatBuffer floatBuffer2 = GLAllocation.createDirectFloatBuffer((int)16);

    public static boolean isInView(Entity ent) {
        frustum.setPosition(ESPUtil.mc.getRenderViewEntity().posX, ESPUtil.mc.getRenderViewEntity().posY, ESPUtil.mc.getRenderViewEntity().posZ);
        return frustum.isBoundingBoxInFrustum(ent.getEntityBoundingBox()) || ent.ignoreFrustumCheck;
    }

    public static Vector3f projectOn2D(float x, float y, float z, int scaleFactor) {
        GL11.glGetFloat((int)2982, (FloatBuffer)floatBuffer1);
        GL11.glGetFloat((int)2983, (FloatBuffer)floatBuffer2);
        GL11.glGetInteger((int)2978, (IntBuffer)intBuffer);
        if (GLU.gluProject((float)x, (float)y, (float)z, (FloatBuffer)floatBuffer1, (FloatBuffer)floatBuffer2, (IntBuffer)intBuffer, (FloatBuffer)windPos)) {
            return new Vector3f(windPos.get(0) / (float)scaleFactor, ((float)ESPUtil.mc.displayHeight - windPos.get(1)) / (float)scaleFactor, windPos.get(2));
        }
        return null;
    }

    public static double[] getInterpolatedPos(Entity entity) {
        float ticks = ESPUtil.mc.timer.renderPartialTicks;
        return new double[]{MathUtils.interpolate(entity.lastTickPosX, entity.posX, ticks) - ESPUtil.mc.getRenderManager().viewerPosX, MathUtils.interpolate(entity.lastTickPosY, entity.posY, ticks) - ESPUtil.mc.getRenderManager().viewerPosY, MathUtils.interpolate(entity.lastTickPosZ, entity.posZ, ticks) - ESPUtil.mc.getRenderManager().viewerPosZ};
    }

    public static AxisAlignedBB getInterpolatedBoundingBox(Entity entity) {
        double[] renderingEntityPos = ESPUtil.getInterpolatedPos(entity);
        double entityRenderWidth = (double)entity.width / 1.5;
        return new AxisAlignedBB(renderingEntityPos[0] - entityRenderWidth, renderingEntityPos[1], renderingEntityPos[2] - entityRenderWidth, renderingEntityPos[0] + entityRenderWidth, renderingEntityPos[1] + (double)entity.height + (entity.isSneaking() ? -0.3 : 0.18), renderingEntityPos[2] + entityRenderWidth).expand(0.15, 0.15, 0.15);
    }

    public static Vector4f getEntityPositionsOn2D(Entity entity) {
        AxisAlignedBB bb = ESPUtil.getInterpolatedBoundingBox(entity);
        List<Vector3f> vectors = Arrays.asList(new Vector3f((float)bb.minX, (float)bb.minY, (float)bb.minZ), new Vector3f((float)bb.minX, (float)bb.maxY, (float)bb.minZ), new Vector3f((float)bb.maxX, (float)bb.minY, (float)bb.minZ), new Vector3f((float)bb.maxX, (float)bb.maxY, (float)bb.minZ), new Vector3f((float)bb.minX, (float)bb.minY, (float)bb.maxZ), new Vector3f((float)bb.minX, (float)bb.maxY, (float)bb.maxZ), new Vector3f((float)bb.maxX, (float)bb.minY, (float)bb.maxZ), new Vector3f((float)bb.maxX, (float)bb.maxY, (float)bb.maxZ));
        Vector4f entityPos = new Vector4f(Float.MAX_VALUE, Float.MAX_VALUE, -1.0f, -1.0f);
        ScaledResolution sr = new ScaledResolution(mc);
        for (Vector3f vector3f : vectors) {
            vector3f = ESPUtil.projectOn2D(vector3f.x, vector3f.y, vector3f.z, sr.getScaleFactor());
            if (vector3f == null || !((double)vector3f.z >= 0.0) || !((double)vector3f.z < 1.0)) continue;
            entityPos.x = Math.min(vector3f.x, entityPos.x);
            entityPos.y = Math.min(vector3f.y, entityPos.y);
            entityPos.z = Math.max(vector3f.x, entityPos.z);
            entityPos.w = Math.max(vector3f.y, entityPos.w);
        }
        return entityPos;
    }
}

