package Shiver.event.type;

import Shiver.event.Event;

public class CancellableEvent
extends Event {
    private boolean cancelled;

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}

