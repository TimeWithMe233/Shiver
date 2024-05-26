package Shiver.ui.click.window.holder;

import Shiver.setting.AbstractSetting;
import Shiver.util.misc.TimerUtil;

public class SettingHolder {
    private AbstractSetting setting;
    private boolean holdingMouse;
    private final TimerUtil timer = new TimerUtil();

    public SettingHolder(AbstractSetting s) {
        this.setting = s;
    }

    public void click() {
        this.timer.reset();
    }

    public <T extends AbstractSetting> T getSetting() {
        return (T)this.setting;
    }

    public boolean isHoldingMouse() {
        return this.holdingMouse;
    }

    public void setHoldingMouse(boolean holdingMouse) {
        this.holdingMouse = holdingMouse;
    }

    public TimerUtil getTimer() {
        return this.timer;
    }
}

