package Shiver.module.impl.player;

import Shiver.event.Listener;
import Shiver.event.impl.PostMotionEvent;
import Shiver.module.Category;
import Shiver.module.Module;
import Shiver.setting.impl.DoubleSetting;

public class Timer
extends Module {
    private final DoubleSetting speed = new DoubleSetting("Speed", 1.1, 0.1, 5.0, 0.1);

    public Timer() {
        super("Timer", Category.PLAYER);
        this.addSettings(this.speed);
    }

    @Override
    public void onDisable() {
        Timer.mc.timer.timerSpeed = 1.0f;
    }

    @Listener
    public void onPostMotion(PostMotionEvent event) {
        Timer.mc.timer.timerSpeed = (float)this.speed.getValue();
    }
}

