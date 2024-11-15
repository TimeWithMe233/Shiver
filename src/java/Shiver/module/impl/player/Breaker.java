package Shiver.module.impl.player;

import Shiver.Shiver;
import Shiver.event.Listener;
import Shiver.event.impl.JumpEvent;
import Shiver.event.impl.MotionEvent;
import Shiver.event.impl.Render3DEvent;
import Shiver.event.impl.StrafeEvent;
import Shiver.event.impl.TickEvent;
import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.setting.impl.BooleanSetting;
import Shiver.setting.impl.IntegerSetting;
import Shiver.setting.impl.ModeSetting;
import Shiver.ui.notification.Notification;
import Shiver.ui.notification.NotificationType;
import Shiver.util.network.PacketUtils;
import Shiver.util.player.FixedRotations;
import Shiver.util.player.RotationsUtil;
import Shiver.util.render.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Mouse;
import tech.skidonion.obfuscator.annotations.NativeObfuscation;

@NativeObfuscation(virtualize = NativeObfuscation.VirtualMachine.TIGER_BLACK)
public class Breaker
extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Through walls", "Through walls", "Break block over", "Hypixel");
    private final IntegerSetting range = new IntegerSetting("Range", 4, 1, 6, 1);
    private final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    private final BooleanSetting moveFix = new BooleanSetting("Move fix", () -> this.rotate.isEnabled(), false);
    private final BooleanSetting renderBlock = new BooleanSetting("Render block", true);
    private BlockPos bedPos;
    private BlockPos miningPos;
    private double blockDamage;
    private FixedRotations rotations;
    private boolean wasDigging;
    private int oldSlot;

    public Breaker() {
        super("BedNuker", Category.PLAYER);
        this.addSettings(this.mode, this.range, this.rotate, this.moveFix, this.renderBlock);
    }

    @Override
    public void onEnable() {
        this.bedPos = null;
        this.rotations = new FixedRotations(Breaker.mc.thePlayer.rotationYaw, Breaker.mc.thePlayer.rotationPitch);
        this.blockDamage = 0.0;
    }

    @Override
    public void onDisable() {
        Breaker.mc.gameSettings.keyBindAttack.pressed = Mouse.isButtonDown((int)0);
        if (this.wasDigging) {
            Shiver.instance.getSlotSpoofHandler().stopSpoofing();
            Breaker.mc.thePlayer.inventory.currentItem = this.oldSlot;
            this.wasDigging = false;
        }
    }

    @Listener
    public void onTick(TickEvent event) {
        if (Breaker.mc.thePlayer.ticksExisted < 10) {
            this.setEnabled(false);
            return;
        }
        this.bedPos = null;
        boolean found = false;
        if (this.rotations == null) {
            this.rotations = new FixedRotations(Breaker.mc.thePlayer.rotationYaw, Breaker.mc.thePlayer.rotationPitch);
        }
        float yaw = this.rotations.getYaw();
        float pitch = this.rotations.getPitch();
        if (this.mode.is("Hypixel")) {
            if (this.bedPos == null) {
                for (double x = Breaker.mc.thePlayer.posX - (double)this.range.getValue(); x <= Breaker.mc.thePlayer.posX + (double)this.range.getValue(); x += 1.0) {
                    for (double y = Breaker.mc.thePlayer.posY + (double)Breaker.mc.thePlayer.getEyeHeight() - (double)this.range.getValue(); y <= Breaker.mc.thePlayer.posY + (double)Breaker.mc.thePlayer.getEyeHeight() + (double)this.range.getValue(); y += 1.0) {
                        for (double z = Breaker.mc.thePlayer.posZ - (double)this.range.getValue(); z <= Breaker.mc.thePlayer.posZ + (double)this.range.getValue(); z += 1.0) {
                            BlockPos pos = new BlockPos(x, y, z);
                            if (!(Breaker.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockBed)) continue;
                            this.bedPos = pos;
                        }
                    }
                }
            }
            if (this.bedPos != null) {
                BlockPos blockToMine = null;
                if (this.isBlockOver(this.bedPos)) {
                    BlockPos posOver = this.bedPos.add(0, 1, 0);
                    float[] rots = RotationsUtil.getRotationsToPosition((double)posOver.getX() + 0.5, posOver.getY() + 1, (double)posOver.getZ() + 0.5);
                    yaw = rots[0];
                    pitch = rots[1];
                    blockToMine = posOver;
                } else {
                    float[] rots = RotationsUtil.getRotationsToPosition((double)this.bedPos.getX() + 0.5, (double)this.bedPos.getY() + 0.5, (double)this.bedPos.getZ() + 0.5);
                    yaw = rots[0];
                    pitch = rots[1];
                    blockToMine = this.bedPos;
                }
                if (blockToMine != null) {
                    Block block = Breaker.mc.theWorld.getBlockState(blockToMine).getBlock();
                    float strength = 0.0f;
                    if (!this.wasDigging) {
                        this.oldSlot = Breaker.mc.thePlayer.inventory.currentItem;
                    }
                    for (int i = 0; i <= 8; ++i) {
                        float slotStrength;
                        ItemStack stack = Breaker.mc.thePlayer.inventory.getStackInSlot(i);
                        if (stack == null || !((slotStrength = stack.getStrVsBlock(block)) > strength)) continue;
                        Breaker.mc.thePlayer.inventory.currentItem = i;
                        strength = slotStrength;
                    }
                    Shiver.instance.getSlotSpoofHandler().startSpoofing(this.oldSlot);
                    Breaker.mc.playerController.syncCurrentPlayItem();
                    this.wasDigging = true;
                    this.mineBlock(blockToMine);
                }
            } else if (this.wasDigging) {
                Breaker.mc.thePlayer.inventory.currentItem = this.oldSlot;
                Shiver.instance.getSlotSpoofHandler().stopSpoofing();
                this.wasDigging = false;
            } else {
                this.oldSlot = Breaker.mc.thePlayer.inventory.currentItem;
            }
            if (this.bedPos != null) {
                // empty if block
            }
        } else {
            for (double x = Breaker.mc.thePlayer.posX - (double)this.range.getValue(); x <= Breaker.mc.thePlayer.posX + (double)this.range.getValue(); x += 1.0) {
                for (double y = Breaker.mc.thePlayer.posY + (double)Breaker.mc.thePlayer.getEyeHeight() - (double)this.range.getValue(); y <= Breaker.mc.thePlayer.posY + (double)Breaker.mc.thePlayer.getEyeHeight() + (double)this.range.getValue(); y += 1.0) {
                    for (double z = Breaker.mc.thePlayer.posZ - (double)this.range.getValue(); z <= Breaker.mc.thePlayer.posZ + (double)this.range.getValue(); z += 1.0) {
                        BlockPos pos = new BlockPos(x, y, z);
                        if (!(Breaker.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockBed) || found) continue;
                        this.bedPos = pos;
                        if (this.mode.is("Break block over") && this.isBlockOver(this.bedPos)) {
                            BlockPos posOver = pos.add(0, 1, 0);
                            Breaker.mc.objectMouseOver = new MovingObjectPosition(new Vec3((double)posOver.getX() + 0.5, (double)(posOver.getY() + 1), (double)posOver.getZ() + 0.5), EnumFacing.UP, posOver);
                            Breaker.mc.gameSettings.keyBindAttack.pressed = true;
                            float[] rots = RotationsUtil.getRotationsToPosition((double)posOver.getX() + 0.5, posOver.getY() + 1, (double)posOver.getZ() + 0.5);
                            yaw = rots[0];
                            pitch = rots[1];
                            this.miningPos = posOver;
                        } else {
                            Breaker.mc.objectMouseOver = new MovingObjectPosition(new Vec3((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5), EnumFacing.UP, this.bedPos);
                            Breaker.mc.gameSettings.keyBindAttack.pressed = true;
                            float[] rots = RotationsUtil.getRotationsToPosition((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5);
                            yaw = rots[0];
                            pitch = rots[1];
                            this.miningPos = pos;
                        }
                        found = true;
                    }
                }
            }
            if (!found) {
                Breaker.mc.gameSettings.keyBindAttack.pressed = Mouse.isButtonDown((int)0) && Breaker.mc.currentScreen == null;
            }
        }
        this.rotations.updateRotations(yaw, pitch);
    }

    private void mineBlock(BlockPos pos) {
        Block block = Breaker.mc.theWorld.getBlockState(pos).getBlock();
        if (block.getMaterial() == Material.air && this.miningPos != null) {
            this.stopMining(this.miningPos);
            return;
        }
        if (this.miningPos == null || !this.miningPos.equals((Object)pos)) {
            this.blockDamage = 0.0;
        }
        this.blockDamage += (double)block.getPlayerRelativeBlockHardness((EntityPlayer)Breaker.mc.thePlayer, Breaker.mc.thePlayer.worldObj, pos);
        PacketUtils.sendPacket((Packet)new C0APacketAnimation());
        if (this.miningPos == null || !this.miningPos.equals((Object)pos)) {
            PacketUtils.sendPacket((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
            if (this.blockDamage >= 1.0) {
                Breaker.mc.theWorld.setBlockToAir(pos);
                this.miningPos = null;
                this.bedPos = null;
            }
        } else if (this.blockDamage >= 1.0) {
            PacketUtils.sendPacket((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.UP));
            Breaker.mc.theWorld.setBlockToAir(pos);
            this.miningPos = null;
            this.bedPos = null;
        }
        this.miningPos = pos;
    }

    private void stopMining(BlockPos pos) {
        Block block = Breaker.mc.theWorld.getBlockState(pos).getBlock();
        PacketUtils.sendPacket((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, pos, EnumFacing.UP));
        Shiver.instance.getNotificationHandler().postNotification(new Notification(NotificationType.WARNING, "BedNuker", "Bed has been breaked", 1000L));
        this.miningPos = null;
    }

    @Listener
    public void onRender3D(Render3DEvent event) {
        if (this.isBreakingBed() && this.renderBlock.isEnabled()) {
            RenderUtil.prepareBoxRender(3.25f, 1.0, 1.0, 1.0, 0.8f);
            RenderUtil.renderBlockBox(mc.getRenderManager(), event.getPartialTicks(), this.miningPos.getX(), this.miningPos.getY(), this.miningPos.getZ());
            RenderUtil.stopBoxRender();
        }
    }

    public boolean isBlockOver(BlockPos pos) {
        BlockPos posOver = pos.add(0, 1, 0);
        Block block = Breaker.mc.theWorld.getBlockState(posOver).getBlock();
        return !(block instanceof BlockAir) && !(block instanceof BlockLiquid);
    }

    public boolean isBreakingBed() {
        return this.bedPos != null;
    }

    @Listener
    public void onStrafe(StrafeEvent event) {
        if (this.bedPos != null && this.rotate.isEnabled() && this.moveFix.isEnabled()) {
            event.setYaw(this.rotations.getYaw());
        }
    }

    @Listener
    public void onJump(JumpEvent event) {
        if (this.bedPos != null && this.rotate.isEnabled() && this.moveFix.isEnabled()) {
            event.setYaw(this.rotations.getYaw());
        }
    }

    @Listener
    public void onMotion(MotionEvent event) {
        if (this.bedPos != null && this.rotate.isEnabled()) {
            event.setYaw(this.rotations.getYaw());
            event.setPitch(this.rotations.getPitch());
        }
    }
}

