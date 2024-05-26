package Shiver.module.impl.ghost;

import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.setting.impl.ModeSetting;

public class DelayRemover
extends Module {
    public final ModeSetting mouseDelayMode = new ModeSetting("Mouse delay", "Normal", "Normal", "Reach gain", "None");

    public DelayRemover() {
        super("Delay remover", Category.GHOST);
        this.addSettings(this.mouseDelayMode);
    }
}

