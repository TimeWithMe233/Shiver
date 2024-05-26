package Shiver.event.impl;

import Shiver.event.Event;

public class KeyPressEvent
extends Event {
    private int key;

    public int getKey() {
        return this.key;
    }

    public KeyPressEvent(int key) {
        this.key = key;
    }
}

