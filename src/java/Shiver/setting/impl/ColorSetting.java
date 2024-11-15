package Shiver.setting.impl;

import Shiver.setting.AbstractSetting;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ColorSetting
extends AbstractSetting {
    private int index;
    private List<String> list;

    public ColorSetting(String name, String t, String ... list) {
        super(name);
        this.list = Arrays.asList(list);
        this.setMode(t);
    }

    public ColorSetting(String name, Supplier<Boolean> visibility, String t, String ... list) {
        super(name, visibility);
        this.list = Arrays.asList(list);
        this.setMode(t);
    }

    public ColorSetting(String name, String displayName, Supplier<Boolean> visibility, String t, String ... list) {
        super(name, displayName, visibility);
        this.list = Arrays.asList(list);
        this.setMode(t);
    }

    public String getMode() {
        if (this.index >= this.list.size() || this.index < 0) {
            this.index = 0;
        }
        return this.list.get(this.index);
    }

    public void setMode(String mode) {
        this.index = this.list.indexOf(mode);
    }

    public boolean is(String mode) {
        if (this.index >= this.list.size() || this.index < 0) {
            this.index = 0;
        }
        return this.list.get(this.index).equals(mode);
    }

    public void increment() {
        this.index = this.index < this.list.size() - 1 ? ++this.index : 0;
    }

    public void decrement() {
        this.index = this.index > 0 ? --this.index : this.list.size() - 1;
    }

    public int getIndex() {
        return this.index;
    }

    public List<String> getList() {
        return this.list;
    }
}

