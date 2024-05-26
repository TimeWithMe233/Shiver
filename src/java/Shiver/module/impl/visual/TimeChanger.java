package Shiver.module.impl.visual;

import Shiver.event.Listener;
import Shiver.event.impl.PacketReceiveEvent;
import Shiver.event.impl.RenderEvent;
import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.setting.impl.DoubleSetting;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

public class TimeChanger
extends Module {
    private final DoubleSetting customTime = new DoubleSetting("Custom time", 18000.0, 0.0, 24000.0, 500.0);

    public TimeChanger() {
        super("Time Changer", Category.VISUAL);
        this.addSettings(this.customTime);
    }

    @Listener
    public void onRender(RenderEvent event) {
        TimeChanger.mc.theWorld.setWorldTime((long)this.customTime.getValue());
    }

    @Listener
    public void onReceive(PacketReceiveEvent event) {
        if (event.getPacket() instanceof S03PacketTimeUpdate) {
            event.setCancelled(true);
        }
    }
}

