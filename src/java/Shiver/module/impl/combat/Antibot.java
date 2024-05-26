package Shiver.module.impl.combat;

import Shiver.Shiver;
import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.setting.impl.BooleanSetting;
import Shiver.setting.impl.IntegerSetting;
import Shiver.util.misc.LogUtil;
import net.minecraft.entity.EntityLivingBase;

public class Antibot
extends Module {
    private final IntegerSetting ticksExisted = new IntegerSetting("Ticks existed", 30, 0, 100, 5);
    public final BooleanSetting advancedDetection = new BooleanSetting("Advanced detection", true);
    public final BooleanSetting debug = new BooleanSetting("Debug", false);
    private Killaura killauraModule;

    public Antibot() {
        super("Antibot", Category.COMBAT);
        this.addSettings(this.ticksExisted, this.advancedDetection, this.debug);
    }

    @Override
    public void onClientStarted() {
        this.killauraModule = Shiver.instance.getModuleManager().getModule(Killaura.class);
    }

    public boolean canAttack(EntityLivingBase entity, Module module) {
        if (!this.isEnabled()) {
            return true;
        }
        if (entity.ticksExisted < this.ticksExisted.getValue()) {
            if (this.debug.isEnabled() && module == this.killauraModule) {
                LogUtil.addChatMessage("Ticks existed antibot : prevented from hitting : " + entity.ticksExisted);
            }
            return false;
        }
        return true;
    }
}

