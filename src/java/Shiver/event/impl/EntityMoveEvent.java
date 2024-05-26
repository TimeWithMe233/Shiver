package Shiver.event.impl;

import Shiver.event.Event;
import net.minecraft.entity.Entity;

public class EntityMoveEvent
extends Event {
    private Entity entity;

    public Entity getEntity() {
        return this.entity;
    }

    public EntityMoveEvent(Entity entity) {
        this.entity = entity;
    }
}

