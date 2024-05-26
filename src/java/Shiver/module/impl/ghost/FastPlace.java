package Shiver.module.impl.ghost;

import Shiver.event.Listener;
import Shiver.event.impl.PacketSendEvent;
import Shiver.event.impl.RenderEvent;
import Shiver.event.impl.TickEvent;
import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.setting.impl.ModeSetting;
import Shiver.util.network.PacketUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Mouse;

public class FastPlace
extends Module {
    private final ModeSetting delay = new ModeSetting("Delay", "Every tick", "Every tick", "Every 2 ticks", "No CPS cap", "Every tick on render");
    private C08PacketPlayerBlockPlacement placement;
    private boolean placedBlock;

    public FastPlace() {
        super("Fastplace", Category.PLAYER);
        this.addSettings(this.delay);
    }

    @Override
    public void onDisable() {
        FastPlace.mc.rightClickDelayTimer = 6;
        this.placement = null;
    }

    @Listener
    public void onTick(TickEvent event) {
        if (this.delay.is("Every tick")) {
            FastPlace.mc.rightClickDelayTimer = 0;
        } else if (this.delay.is("Every 2 ticks") && FastPlace.mc.thePlayer.ticksExisted % 2 == 0) {
            FastPlace.mc.rightClickDelayTimer = 0;
        }
        if (this.delay.is("Every tick on render") && this.placement != null) {
            PacketUtils.sendPacketFinal((Packet)this.placement);
            this.placement = null;
        }
        this.placedBlock = false;
    }

    @Listener
    public void onRender(RenderEvent event) {
        if ((this.delay.is("No CPS cap") || this.delay.is("Every tick on render") && !this.placedBlock) && (FastPlace.mc.gameSettings.keyBindUseItem.pressed || Mouse.isButtonDown((int)1))) {
            FastPlace.mc.rightClickDelayTimer = 0;
            mc.rightClickMouse();
        }
    }

    @Listener
    public void onSend(PacketSendEvent event) {
        if (event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            C08PacketPlayerBlockPlacement packet = (C08PacketPlayerBlockPlacement)event.getPacket();
            if (this.delay.is("No CPS cap") || this.delay.is("Every tick on render")) {
                if (packet.getPosition().equals((Object)new BlockPos(-1, -1, -1))) {
                    event.setCancelled(true);
                } else {
                    this.placedBlock = true;
                    if (this.delay.is("Every tick on render")) {
                        event.setCancelled(true);
                        this.placement = packet;
                    }
                }
            }
        }
    }
}

