package Shiver.event.impl;

import Shiver.event.Event;

public class RenderEvent
extends Event {
    private float partialTicks;

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public RenderEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}

