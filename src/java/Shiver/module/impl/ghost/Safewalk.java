package Shiver.module.impl.ghost;

import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.setting.impl.BooleanSetting;

public class Safewalk
extends Module {
    public final BooleanSetting offGround = new BooleanSetting("Offground", false);

    public Safewalk() {
        super("Safewalk", Category.GHOST);
        this.addSettings(this.offGround);
    }
}

