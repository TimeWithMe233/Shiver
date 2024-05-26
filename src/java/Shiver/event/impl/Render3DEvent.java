package Shiver.event.impl;

import Shiver.event.Event;

public class Render3DEvent
extends Event {
    private float partialTicks;

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}

