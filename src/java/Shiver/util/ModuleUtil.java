package Shiver.util;

import Shiver.Shiver;
import Shiver.module.Module;
import Shiver.module.ModuleManager;
import Shiver.module.impl.combat.Killaura;
import Shiver.module.impl.combat.TargetStrafe;
import Shiver.module.impl.combat.Teams;
import Shiver.module.impl.ghost.DelayRemover;
import Shiver.module.impl.ghost.Reach;
import Shiver.module.impl.movement.Blink;
import Shiver.module.impl.movement.Fly;
import Shiver.module.impl.movement.Longjump;
import Shiver.module.impl.movement.Speed;
import Shiver.module.impl.visual.Animations;
import Shiver.module.impl.visual.HUD;

public class ModuleUtil {
    private static ModuleManager moduleManager;

    private static ModuleManager getModuleManager() {
        if (moduleManager == null) {
            moduleManager = Shiver.instance.getModuleManager();
        }
        return moduleManager;
    }

    public static Killaura getKillaura() {
        return ModuleUtil.getModuleManager().getModule(Killaura.class);
    }

    public static Reach getReach() {
        return ModuleUtil.getModuleManager().getModule(Reach.class);
    }

    public static TargetStrafe getTargetStrafe() {
        return ModuleUtil.getModuleManager().getModule(TargetStrafe.class);
    }

    public static Teams getTeams() {
        return ModuleUtil.getModuleManager().getModule(Teams.class);
    }

    public static Fly getFly() {
        return ModuleUtil.getModuleManager().getModule(Fly.class);
    }

    public static DelayRemover getDelayRemover() {
        return ModuleUtil.getModuleManager().getModule(DelayRemover.class);
    }

    public static Speed getSpeed() {
        return ModuleUtil.getModuleManager().getModule(Speed.class);
    }

    public static HUD getHUD() {
        return ModuleUtil.getModuleManager().getModule(HUD.class);
    }

    public static Longjump getLongjump() {
        return ModuleUtil.getModuleManager().getModule(Longjump.class);
    }

    public static Animations getAnimations() {
        return ModuleUtil.getModuleManager().getModule(Animations.class);
    }

    public static Blink getBlink() {
        return ModuleUtil.getModuleManager().getModule(Blink.class);
    }

    public static void startBlinking(Module m, boolean transactionsAtLast) {
        ModuleUtil.getBlink().startBlinking(m, transactionsAtLast);
    }

    public static void stopBlinking() {
        ModuleUtil.getBlink().stopBlinking();
    }
}

