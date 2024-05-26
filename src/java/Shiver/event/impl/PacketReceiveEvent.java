package Shiver.event.impl;

import Shiver.event.type.CancellableEvent;
import net.minecraft.network.Packet;

public class PacketReceiveEvent
extends CancellableEvent {
    private Packet packet;

    public <T extends Packet> T getPacket() {
        return (T)this.packet;
    }

    public PacketReceiveEvent(Packet packet) {
        this.packet = packet;
    }
}

