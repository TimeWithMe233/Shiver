package Shiver.setting.impl;

import Shiver.setting.AbstractSetting;
import java.util.function.Supplier;

public class CustomDoubleSetting
extends AbstractSetting {
    private double value;
    private boolean typing;

    public CustomDoubleSetting(String name, double value) {
        super(name);
        this.value = value;
    }

    public CustomDoubleSetting(String name, Supplier<Boolean> visibility, double value) {
        super(name, visibility);
        this.value = value;
    }

    public CustomDoubleSetting(String name, String displayName, Supplier<Boolean> visibility, double value) {
        super(name, displayName, visibility);
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }

    public boolean isTyping() {
        return this.typing;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setTyping(boolean typing) {
        this.typing = typing;
    }
}

