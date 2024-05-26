package Shiver.module.impl.visual;

import Shiver.event.Listener;
import Shiver.event.impl.UpdateEvent;
import Shiver.module.Category;
import Shiver.module.Module;

public class Fullbright
extends Module {
    public Fullbright() {
        super("Fullbright", Category.VISUAL);
    }

    @Override
    public void onEnable() {
        Fullbright.mc.gameSettings.gammaSetting = 100.0f;
    }

    @Override
    public void onDisable() {
        Fullbright.mc.gameSettings.gammaSetting = 1.0f;
    }

    @Listener
    public void onUpdate(UpdateEvent event) {
        Fullbright.mc.gameSettings.gammaSetting = 100.0f;
    }
}

