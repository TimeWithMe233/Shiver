package Shiver.module.impl.visual;

import Shiver.Shiver;
import Shiver.module.Category;
import Shiver.module.Module;

public class SelfDestruct
extends Module {
    public SelfDestruct() {
        super("Self Destruct", Category.EXPLOIT);
    }

    @Override
    public void onEnable() {
        Shiver.instance.getModuleManager().modules.forEach(m -> m.setEnabled(false));
        Shiver.instance.getPacketDelayHandler().stopAll();
        Shiver.instance.getPacketBlinkHandler().stopBlinking();
        Shiver.instance.getCameraHandler().setFreelooking(false);
        Shiver.instance.getSlotSpoofHandler().stopSpoofing();
        mc.displayGuiScreen(null);
        SelfDestruct.mc.timer.timerSpeed = 1.0f;
        Shiver.instance.setDestructed(true);
    }
}

