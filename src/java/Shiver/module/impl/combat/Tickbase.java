package Shiver.module.impl.combat;

import Shiver.Shiver;
import Shiver.event.Listener;
import Shiver.event.impl.PostMotionEvent;
import Shiver.event.impl.RenderEvent;
import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.setting.impl.IntegerSetting;

public class Tickbase
extends Module {
    private Killaura killauraModule;
    private int counter = -1;
    public boolean freezing;
    private final IntegerSetting ticks = new IntegerSetting("Ticks", 3, 1, 10, 1);

    public Tickbase() {
        super("Tickbase", Category.COMBAT);
        this.addSettings(this.ticks);
    }

    @Override
    public void onEnable() {
        this.counter = -1;
        this.freezing = false;
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onClientStarted() {
        this.killauraModule = Shiver.instance.getModuleManager().getModule(Killaura.class);
    }

    public int getExtraTicks() {
        if (this.counter-- > 0) {
            return -1;
        }
        this.freezing = false;
        if (this.killauraModule.isEnabled() && (this.killauraModule.getTarget() == null || this.killauraModule.getDistanceToEntity(this.killauraModule.getTarget()) > this.killauraModule.range.getValue()) && this.killauraModule.findTarget(!this.killauraModule.mode.is("Fast Switch"), this.killauraModule.startingRange.getValue() + 0.75) != null && Tickbase.mc.thePlayer.hurtTime <= 2) {
            this.counter = this.ticks.getValue();
            return this.counter;
        }
        return 0;
    }

    @Listener
    public void onPostMotion(PostMotionEvent event) {
        if (this.freezing) {
            Tickbase.mc.thePlayer.posX = Tickbase.mc.thePlayer.lastTickPosX;
            Tickbase.mc.thePlayer.posY = Tickbase.mc.thePlayer.lastTickPosY;
            Tickbase.mc.thePlayer.posZ = Tickbase.mc.thePlayer.lastTickPosZ;
        }
    }

    @Listener
    public void onRender(RenderEvent event) {
        if (this.freezing) {
            Tickbase.mc.timer.renderPartialTicks = 0.0f;
        }
    }
}

