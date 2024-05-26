package Shiver.module.impl.movement;

import Shiver.Shiver;
import Shiver.event.Listener;
import Shiver.event.impl.ItemRenderEvent;
import Shiver.event.impl.PacketSendEvent;
import Shiver.event.impl.PostMotionEvent;
import Shiver.event.impl.SlowdownEvent;
import Shiver.event.impl.UpdateEvent;
import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.module.impl.combat.Killaura;
import Shiver.setting.impl.BooleanSetting;
import Shiver.setting.impl.DoubleSetting;
import Shiver.setting.impl.IntegerSetting;
import Shiver.setting.impl.ModeSetting;
import Shiver.util.network.PacketUtils;
import java.util.Objects;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Mouse;
import tech.skidonion.obfuscator.annotations.NativeObfuscation;

@NativeObfuscation(virtualize = NativeObfuscation.VirtualMachine.TIGER_BLACK)
public class Noslow
extends Module {
    private final ModeSetting swordMethod = new ModeSetting("Sword method", "Vanilla", "Vanilla", "Watchdog", "NCP", "AAC4", "AAC5", "Spoof", "Spoof2", "Blink", "None");
    private final ModeSetting consumableMethod = new ModeSetting("Eating method", "Vanilla", "Vanilla", "Hypixel", "AAC4", "AAC5", "None");
    private final DoubleSetting forward = new DoubleSetting("Forward", 1.0, 0.2, 1.0, 0.05);
    private final DoubleSetting strafe = new DoubleSetting("Strafe", 1.0, 0.2, 1.0, 0.05);
    private final IntegerSetting blinkTicks = new IntegerSetting("Blink ticks", () -> this.swordMethod.is("Blink"), 5, 2, 10, 1);
    public final BooleanSetting allowSprinting = new BooleanSetting("Allow sprinting", true);
    private Killaura killauraModule;
    private boolean lastUsingItem;
    private int ticks;
    private int lastSlot;
    private boolean wasEating;

    public Noslow() {
        super("Noslow", Category.MOVEMENT);
        this.addSettings(this.swordMethod, this.consumableMethod, this.forward, this.strafe, this.blinkTicks, this.allowSprinting);
    }

    @Override
    public void onEnable() {
        this.wasEating = false;
        this.lastUsingItem = false;
        this.lastSlot = Noslow.mc.thePlayer.inventory.currentItem;
        this.ticks = 0;
    }

    @Override
    public void onDisable() {
        Shiver.instance.getPacketBlinkHandler().stopBlinking();
    }

    @Override
    public void onClientStarted() {
        this.killauraModule = Shiver.instance.getModuleManager().getModule(Killaura.class);
    }

    @Listener
    public void onUpdate(UpdateEvent event) {
        if (this.isUsingItem()) {
            if (this.isBlocking()) {
                switch (this.swordMethod.getMode()) {
                    case "NCP": {
                        PacketUtils.releaseUseItem(true);
                        break;
                    }
                    case "AAC4": {
                        if (Noslow.mc.thePlayer.ticksExisted % 2 != 0) break;
                        PacketUtils.releaseUseItem(true);
                        break;
                    }
                    case "AAC5": {
                        if (!this.lastUsingItem) break;
                        PacketUtils.sendBlocking(true, false);
                        break;
                    }
                    case "Spoof": {
                        int slot = Noslow.mc.thePlayer.inventory.currentItem;
                        PacketUtils.sendPacket((Packet)new C09PacketHeldItemChange(slot < 8 ? slot + 1 : 0));
                        PacketUtils.sendPacket((Packet)new C09PacketHeldItemChange(slot));
                        if (!this.lastUsingItem) break;
                        PacketUtils.sendBlocking(true, false);
                        break;
                    }
                    case "Spoof2": {
                        PacketUtils.sendPacket((Packet)new C09PacketHeldItemChange(Noslow.mc.thePlayer.inventory.currentItem));
                        break;
                    }
                    case "Watchdog": {
                        PacketUtils.sendBlocking(true, false);
                        if (!Noslow.mc.thePlayer.isUsingItem() || Noslow.mc.thePlayer.isBlocking() || Noslow.mc.thePlayer.ticksExisted % 3 != 0) break;
                        PacketUtils.sendPacketNoEvent((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), EnumFacing.UP.getIndex(), null, 0.0f, 0.0f, 0.0f));
                    }
                }
            } else {
                switch (this.consumableMethod.getMode()) {
                    case "AAC4": {
                        if (!this.lastUsingItem) break;
                        int slot = Noslow.mc.thePlayer.inventory.currentItem;
                        PacketUtils.sendPacket((Packet)new C09PacketHeldItemChange(slot < 8 ? slot + 1 : 0));
                        PacketUtils.sendPacket((Packet)new C09PacketHeldItemChange(slot));
                        break;
                    }
                    case "AAC5": {
                        if (!this.lastUsingItem) break;
                        PacketUtils.sendBlocking(true, false);
                    }
                }
            }
        }
        if (this.swordMethod.is("Blink")) {
            if (this.isHoldingSword() && this.pressingUseItem()) {
                if (this.ticks == 1) {
                    Shiver.instance.getPacketBlinkHandler().releasePackets();
                    Shiver.instance.getPacketBlinkHandler().startBlinking();
                }
                if (this.ticks > 0 && this.ticks < this.blinkTicks.getValue()) {
                    Noslow.mc.gameSettings.keyBindUseItem.pressed = false;
                }
                if (this.ticks == this.blinkTicks.getValue()) {
                    Shiver.instance.getPacketBlinkHandler().stopBlinking();
                    Noslow.mc.gameSettings.keyBindUseItem.pressed = true;
                    this.ticks = 0;
                }
                ++this.ticks;
            } else {
                Shiver.instance.getPacketBlinkHandler().stopBlinking();
                this.ticks = 0;
            }
        }
        if (this.consumableMethod.is("Hypixel") && Noslow.mc.thePlayer.isUsingItem() && !Noslow.mc.thePlayer.isBlocking() && Noslow.mc.thePlayer.ticksExisted % 3 == 0) {
            PacketUtils.sendPacketNoEvent((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), EnumFacing.UP.getIndex(), null, 0.0f, 0.0f, 0.0f));
        }
    }

    @Listener
    public void onPostMotion(PostMotionEvent event) {
        boolean usingItem = Noslow.mc.thePlayer.isUsingItem();
        if (usingItem) {
            if (this.isBlocking()) {
                switch (this.swordMethod.getMode()) {
                    case "NCP": {
                        if (!this.isBlocking()) break;
                        PacketUtils.sendBlocking(true, false);
                        break;
                    }
                    case "AAC4": {
                        if (Noslow.mc.thePlayer.ticksExisted % 2 != 0) break;
                        PacketUtils.sendBlocking(true, false);
                    }
                }
            } else {
                Objects.requireNonNull(this.consumableMethod.getMode());
            }
        }
        this.lastUsingItem = usingItem;
        this.wasEating = usingItem && Noslow.mc.thePlayer.getHeldItem() != null && (Noslow.mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || Noslow.mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion);
    }

    @Listener
    public void onSlowdown(SlowdownEvent event) {
        if (!(this.isBlocking() && this.swordMethod.is("None") || !this.isBlocking() && this.consumableMethod.is("None"))) {
            event.setForward((float)this.forward.getValue());
            event.setStrafe((float)this.strafe.getValue());
            event.setAllowedSprinting(this.allowSprinting.isEnabled());
        }
    }

    @Listener
    public void onSend(PacketSendEvent event) {
        C07PacketPlayerDigging packet;
        if (event.getPacket() instanceof C07PacketPlayerDigging && (packet = (C07PacketPlayerDigging)event.getPacket()).getStatus() == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM && this.isHoldingSword() && this.swordMethod.is("Spoof")) {
            event.setCancelled(true);
            int slot = Noslow.mc.thePlayer.inventory.currentItem;
            PacketUtils.sendPacketFinal((Packet)new C09PacketHeldItemChange(slot < 8 ? slot + 1 : 0));
            PacketUtils.sendPacketFinal((Packet)new C09PacketHeldItemChange(slot));
        }
    }

    @Listener
    public void onItemRender(ItemRenderEvent event) {
        if (this.isHoldingSword() && this.pressingUseItem() && this.swordMethod.is("Blink")) {
            event.setRenderBlocking(true);
        }
        if (this.consumableMethod.is("Hypixel") && this.ticks > 1) {
            event.setRenderBlocking(true);
        }
    }

    public boolean isBlocking() {
        return Noslow.mc.thePlayer.isUsingItem() && this.isHoldingSword();
    }

    public boolean isUsingItem() {
        return Noslow.mc.thePlayer.isUsingItem() && (!this.killauraModule.isEnabled() || this.killauraModule.getTarget() == null || this.killauraModule.autoblock.is("None"));
    }

    public boolean isHoldingSword() {
        return Noslow.mc.thePlayer.getHeldItem() != null && Noslow.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
    }

    public boolean pressingUseItem() {
        return !(Noslow.mc.currentScreen instanceof GuiInventory) && !(Noslow.mc.currentScreen instanceof GuiChest) && Mouse.isButtonDown((int)1);
    }

    @Override
    public String getSuffix() {
        return this.swordMethod.getMode() + "," + this.consumableMethod.getMode();
    }
}

