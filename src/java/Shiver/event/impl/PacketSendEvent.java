package Shiver.event.impl;

import Shiver.event.type.CancellableEvent;
import net.minecraft.network.Packet;

public class PacketSendEvent
extends CancellableEvent {
    private Packet packet;

    public <T extends Packet> T getPacket() {
        return (T)this.packet;
    }

    public PacketSendEvent(Packet packet) {
        this.packet = packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}

