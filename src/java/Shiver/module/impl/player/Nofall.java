package Shiver.module.impl.player;

import Shiver.event.Listener;
import Shiver.event.impl.MotionEvent;
import Shiver.event.impl.TickEvent;
import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.setting.impl.ModeSetting;
import Shiver.util.ModuleUtil;
import Shiver.util.network.PacketUtils;
import Shiver.util.world.WorldUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Nofall
extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Edit", "Packet", "Edit", "Noground", "Blink");
    private double fallDistance;
    private boolean blinking;
    private boolean prevOnGround;

    public Nofall() {
        super("Nofall", Category.PLAYER);
        this.addSettings(this.mode);
    }

    @Override
    public void onEnable() {
        this.fallDistance = Nofall.mc.thePlayer.fallDistance;
    }

    @Override
    public void onDisable() {
        if (this.blinking) {
            ModuleUtil.stopBlinking();
            this.blinking = false;
        }
    }

    @Listener
    public void onTick(TickEvent event) {
    }

    @Listener
    public void onMotion(MotionEvent event) {
        if (Nofall.mc.thePlayer.onGround) {
            this.fallDistance = 0.0;
        } else if (Nofall.mc.thePlayer.motionY < 0.0) {
            this.fallDistance += -Nofall.mc.thePlayer.motionY;
        }
        switch (this.mode.getMode()) {
            case "Packet": {
                if (!(this.fallDistance >= 3.0)) break;
                PacketUtils.sendPacket((Packet)new C03PacketPlayer(true));
                this.fallDistance = 0.0;
                break;
            }
            case "Edit": {
                if (!(this.fallDistance >= 3.0)) break;
                event.setOnGround(true);
                this.fallDistance = 0.0;
                break;
            }
            case "Blink": {
                if (Nofall.mc.thePlayer.onGround) {
                    if (this.blinking) {
                        ModuleUtil.stopBlinking();
                        this.blinking = false;
                    }
                    this.prevOnGround = true;
                    break;
                }
                if (this.prevOnGround) {
                    if (this.shouldBlink()) {
                        ModuleUtil.startBlinking(this, true);
                        this.blinking = true;
                    }
                    this.prevOnGround = false;
                    break;
                }
                if (!WorldUtil.isBlockUnder() || !ModuleUtil.getBlink().isEnabled() || !(this.fallDistance >= 3.0)) break;
                PacketUtils.sendPacket((Packet)new C03PacketPlayer(true));
                this.fallDistance = 0.0;
                break;
            }
            case "Noground": {
                event.setOnGround(false);
            }
        }
    }

    private boolean shouldBlink() {
        return !Nofall.mc.thePlayer.onGround && !WorldUtil.isBlockUnder(3) && WorldUtil.isBlockUnder();
    }
}

