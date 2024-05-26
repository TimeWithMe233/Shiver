package Shiver.event.impl;

import Shiver.event.Event;

public class Alternative3DEvent
extends Event {
    private final float partialTicks;

    public Alternative3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

