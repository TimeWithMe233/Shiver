package Shiver.module.impl.movement;

import Shiver.event.Listener;
import Shiver.event.impl.TickEvent;
import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.util.network.ServerUtil;

public class Sprint
extends Module {
    public Sprint() {
        super("Sprint", Category.MOVEMENT);
        this.setEnabledSilently(true);
    }

    @Override
    public void onDisable() {
        Sprint.mc.gameSettings.keyBindSprint.pressed = false;
    }

    @Listener(value=1)
    public void onTick(TickEvent event) {
        Sprint.mc.gameSettings.keyBindSprint.pressed = true;
        if (Sprint.mc.thePlayer.moveForward <= 0.0f && ServerUtil.isOnHypixel()) {
            Sprint.mc.thePlayer.setSprinting(false);
        }
    }
}

