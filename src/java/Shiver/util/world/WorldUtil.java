package Shiver.util.world;

import Shiver.util.IMinecraft;
import Shiver.util.player.MovementUtil;
import Shiver.util.player.RotationsUtil;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class WorldUtil
implements IMinecraft {
    public static BlockInfo getBlockInfo(double x, double y, double z, int maxRange) {
        return WorldUtil.getBlockInfo(new BlockPos(x, y, z), maxRange);
    }

    public static BlockInfo getBlockInfo(BlockPos pos, int maxRange) {
        EnumFacing playerDirectionFacing = WorldUtil.getHorizontalFacing(MovementUtil.getPlayerDirection()).getOpposite();
        ArrayList<EnumFacing> facingValues = new ArrayList<EnumFacing>();
        facingValues.add(playerDirectionFacing);
        for (EnumFacing facing : EnumFacing.values()) {
            if (facing == playerDirectionFacing || facing == EnumFacing.UP) continue;
            facingValues.add(facing);
        }
        CopyOnWriteArrayList<BlockPos> aaa = new CopyOnWriteArrayList<BlockPos>();
        aaa.add(pos);
        for (int i = 0; i < maxRange; ++i) {
            ArrayList ccc = new ArrayList(aaa);
            if (!aaa.isEmpty()) {
                for (BlockPos bbbb : aaa) {
                    for (EnumFacing facing : facingValues) {
                        BlockPos n = bbbb.offset(facing);
                        if (WorldUtil.isAirOrLiquid(n)) {
                            aaa.add(n);
                            continue;
                        }
                        return new BlockInfo(n, facing.getOpposite());
                    }
                }
            }
        }
        return null;
    }

    public static EnumFacing getHorizontalFacing(float yaw) {
        return EnumFacing.getHorizontal((int)(MathHelper.floor_double((double)((double)(yaw * 4.0f / 360.0f) + 0.5)) & 3));
    }

    public static Vec3 getVec3(BlockPos pos, EnumFacing facing, boolean randomised) {
        Vec3 vec3 = new Vec3((Vec3i)pos);
        double amountX = 0.5;
        double amountY = 0.5;
        double amountZ = 0.5;
        if (randomised) {
            amountX = 0.45 + Math.random() * 0.1;
            amountY = 0.05 + Math.random() * 0.1;
            amountZ = 0.45 + Math.random() * 0.1;
        }
        if (facing == EnumFacing.UP) {
            vec3 = vec3.addVector(amountX, 1.0, amountZ);
        } else if (facing == EnumFacing.DOWN) {
            vec3 = vec3.addVector(amountX, 0.0, amountZ);
        } else if (facing == EnumFacing.EAST) {
            vec3 = vec3.addVector(1.0, amountY, amountZ);
        } else if (facing == EnumFacing.WEST) {
            vec3 = vec3.addVector(0.0, amountY, amountZ);
        } else if (facing == EnumFacing.NORTH) {
            vec3 = vec3.addVector(amountX, amountY, 0.0);
        } else if (facing == EnumFacing.SOUTH) {
            vec3 = vec3.addVector(amountX, amountY, 1.0);
        }
        return vec3;
    }

    public static Vec3 getVec3ClosestFromRots(final BlockPos pos, final EnumFacing facing, final boolean randomised, final float yaw, final float pitch) {
        double smallestDiff = Double.MAX_VALUE;
        Vec3 finalVec3 = null;
        Vec3 modifiedVec3;
        final Vec3 originalVec3 = modifiedVec3 = new Vec3((Vec3i)pos);
        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
            final int y = (facing == EnumFacing.UP) ? 1 : 0;
            final double random1 = randomised ? (Math.random() * 0.01 - 0.005) : 0.0;
            final double random2 = randomised ? (Math.random() * 0.04) : 0.0;
            for (double amount1 = 0.05; amount1 <= 0.95; amount1 += 0.025) {
                for (double amount2 = 0.05; amount2 <= 0.95; amount2 += 0.025) {
                    modifiedVec3 = originalVec3.addVector(amount1 + random1, (double)y, amount2 + random2);
                    final float[] rots = RotationsUtil.getRotationsToPosition(modifiedVec3.xCoord, modifiedVec3.yCoord, modifiedVec3.zCoord);
                    final double yawDiff = Math.abs(rots[0] - yaw);
                    final double pitchDiff = Math.abs(rots[1] - pitch);
                    final double diff = Math.hypot(yawDiff, pitchDiff);
                    if (diff < smallestDiff) {
                        smallestDiff = diff;
                        finalVec3 = modifiedVec3;
                    }
                }
            }
        }
        else {
            final double random3 = randomised ? (Math.random() * 0.01 - 0.005) : 0.0;
            final double random4 = randomised ? (Math.random() * 0.04) : 0.0;
            for (double amount3 = 0.05; amount3 <= 0.95; amount3 += 0.025) {
                if (facing == EnumFacing.EAST) {
                    modifiedVec3 = originalVec3.addVector(1.0, 0.05 + random4, amount3 + random3);
                }
                else if (facing == EnumFacing.WEST) {
                    modifiedVec3 = originalVec3.addVector(0.0, 0.05 + random4, amount3 + random3);
                }
                else if (facing == EnumFacing.NORTH) {
                    modifiedVec3 = originalVec3.addVector(amount3 + random3, 0.05 + random4, 0.0);
                }
                else if (facing == EnumFacing.SOUTH) {
                    modifiedVec3 = originalVec3.addVector(amount3 + random3, 0.05 + random4, 1.0);
                }
                final float[] rots2 = RotationsUtil.getRotationsToPosition(modifiedVec3.xCoord, modifiedVec3.yCoord, modifiedVec3.zCoord);
                final double yawDiff2 = Math.abs(rots2[0] - yaw);
                final double pitchDiff2 = Math.abs(rots2[1] - pitch);
                final double diff2 = Math.hypot(yawDiff2, pitchDiff2);
                if (diff2 < smallestDiff) {
                    smallestDiff = diff2;
                    finalVec3 = modifiedVec3;
                }
            }
        }
        return finalVec3;
    }

    public static boolean isAir(BlockPos pos) {
        Block block = WorldUtil.mc.theWorld.getBlockState(pos).getBlock();
        return block instanceof BlockAir;
    }

    public static boolean isAirOrLiquid(BlockPos pos) {
        Block block = WorldUtil.mc.theWorld.getBlockState(pos).getBlock();
        return block instanceof BlockAir || block instanceof BlockLiquid;
    }

    public static boolean isOverAirOrLiquid() {
        return WorldUtil.isAirOrLiquid(new BlockPos(WorldUtil.mc.thePlayer.posX, WorldUtil.mc.thePlayer.posY - 1.0, WorldUtil.mc.thePlayer.posZ));
    }

    public static MovingObjectPosition raytrace(float yaw, float pitch) {
        float blockReachDistance = WorldUtil.mc.playerController.getBlockReachDistance();
        Vec3 vec3 = new Vec3(WorldUtil.mc.thePlayer.posX, WorldUtil.mc.thePlayer.posY + (double)WorldUtil.mc.thePlayer.getEyeHeight(), WorldUtil.mc.thePlayer.posZ);
        Vec3 vec31 = WorldUtil.mc.thePlayer.getVectorForRotation(pitch, yaw);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * 1000.0, vec31.yCoord * 1000.0, vec31.zCoord * 1000.0);
        return WorldUtil.mc.theWorld.rayTraceBlocks(vec3, vec32, false, false, true);
    }

    public static MovingObjectPosition raytraceLegit(float yaw, float pitch, float lastYaw, float lastPitch) {
        float partialTicks = WorldUtil.mc.timer.renderPartialTicks;
        float blockReachDistance = WorldUtil.mc.playerController.getBlockReachDistance();
        Vec3 vec3 = WorldUtil.mc.thePlayer.getPositionEyes(partialTicks);
        float f = lastPitch + (pitch - lastPitch) * partialTicks;
        float f1 = lastYaw + (yaw - lastYaw) * partialTicks;
        Vec3 vec31 = WorldUtil.mc.thePlayer.getVectorForRotation(f, f1);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * (double)blockReachDistance, vec31.yCoord * (double)blockReachDistance, vec31.zCoord * (double)blockReachDistance);
        return WorldUtil.mc.theWorld.rayTraceBlocks(vec3, vec32, false, false, true);
    }

    public static boolean isBlockUnder() {
        if (WorldUtil.mc.thePlayer.posY < 0.0) {
            return false;
        }
        for (int offset = 0; offset < (int)WorldUtil.mc.thePlayer.posY + 2; offset += 2) {
            AxisAlignedBB bb = WorldUtil.mc.thePlayer.getEntityBoundingBox().offset(0.0, (double)(-offset), 0.0);
            if (WorldUtil.mc.theWorld.getCollidingBoundingBoxes((Entity)WorldUtil.mc.thePlayer, bb).isEmpty()) continue;
            return true;
        }
        return false;
    }

    public static boolean isBlockUnder(int distance) {
        for (int y = (int)WorldUtil.mc.thePlayer.posY; y >= (int)WorldUtil.mc.thePlayer.posY - distance; --y) {
            if (WorldUtil.mc.theWorld.getBlockState(new BlockPos(WorldUtil.mc.thePlayer.posX, (double)y, WorldUtil.mc.thePlayer.posZ)).getBlock() instanceof BlockAir) continue;
            return true;
        }
        return false;
    }

    public static boolean negativeExpand(double negativeExpandValue) {
        return WorldUtil.mc.theWorld.getBlockState(new BlockPos(WorldUtil.mc.thePlayer.posX + negativeExpandValue, WorldUtil.mc.thePlayer.posY - 1.0, WorldUtil.mc.thePlayer.posZ + negativeExpandValue)).getBlock() instanceof BlockAir && WorldUtil.mc.theWorld.getBlockState(new BlockPos(WorldUtil.mc.thePlayer.posX - negativeExpandValue, WorldUtil.mc.thePlayer.posY - 1.0, WorldUtil.mc.thePlayer.posZ - negativeExpandValue)).getBlock() instanceof BlockAir && WorldUtil.mc.theWorld.getBlockState(new BlockPos(WorldUtil.mc.thePlayer.posX - negativeExpandValue, WorldUtil.mc.thePlayer.posY - 1.0, WorldUtil.mc.thePlayer.posZ)).getBlock() instanceof BlockAir && WorldUtil.mc.theWorld.getBlockState(new BlockPos(WorldUtil.mc.thePlayer.posX + negativeExpandValue, WorldUtil.mc.thePlayer.posY - 1.0, WorldUtil.mc.thePlayer.posZ)).getBlock() instanceof BlockAir && WorldUtil.mc.theWorld.getBlockState(new BlockPos(WorldUtil.mc.thePlayer.posX, WorldUtil.mc.thePlayer.posY - 1.0, WorldUtil.mc.thePlayer.posZ + negativeExpandValue)).getBlock() instanceof BlockAir && WorldUtil.mc.theWorld.getBlockState(new BlockPos(WorldUtil.mc.thePlayer.posX, WorldUtil.mc.thePlayer.posY - 1.0, WorldUtil.mc.thePlayer.posZ - negativeExpandValue)).getBlock() instanceof BlockAir;
    }

    public static boolean negativeExpand(double posX, double posY, double posZ, double negativeExpandValue) {
        return WorldUtil.mc.theWorld.getBlockState(new BlockPos(posX + negativeExpandValue, posY - 1.0, posZ + negativeExpandValue)).getBlock() instanceof BlockAir && WorldUtil.mc.theWorld.getBlockState(new BlockPos(posX - negativeExpandValue, posY - 1.0, posZ - negativeExpandValue)).getBlock() instanceof BlockAir && WorldUtil.mc.theWorld.getBlockState(new BlockPos(posX - negativeExpandValue, posY - 1.0, posZ)).getBlock() instanceof BlockAir && WorldUtil.mc.theWorld.getBlockState(new BlockPos(posX + negativeExpandValue, posY - 1.0, posZ)).getBlock() instanceof BlockAir && WorldUtil.mc.theWorld.getBlockState(new BlockPos(posX, posY - 1.0, posZ + negativeExpandValue)).getBlock() instanceof BlockAir && WorldUtil.mc.theWorld.getBlockState(new BlockPos(posX, posY - 1.0, posZ - negativeExpandValue)).getBlock() instanceof BlockAir;
    }
}

