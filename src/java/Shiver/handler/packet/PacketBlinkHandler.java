package Shiver.handler.packet;

import Shiver.Shiver;
import Shiver.event.Listener;
import Shiver.event.impl.PacketSendEvent;
import Shiver.util.IMinecraft;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

public class PacketBlinkHandler
implements IMinecraft {
    private boolean blinking;
    private boolean clearedPackets;
    private final CopyOnWriteArrayList<Packet> packetsQueue = new CopyOnWriteArrayList();

    public PacketBlinkHandler() {
        Shiver.instance.getEventManager().register(this);
    }

    @Listener
    public void onSend(PacketSendEvent event) {
        if (mc.isSingleplayer()) {
            return;
        }
        if (PacketBlinkHandler.mc.thePlayer == null || PacketBlinkHandler.mc.thePlayer.ticksExisted < 5) {
            if (!this.clearedPackets) {
                this.packetsQueue.clear();
                this.stopBlinking();
                this.clearedPackets = true;
            }
        } else {
            this.clearedPackets = false;
        }
        if (!event.isCancelled() && this.blinking) {
            event.setCancelled(true);
            this.packetsQueue.add((Packet)event.getPacket());
        }
    }

    public void startBlinking() {
        this.blinking = true;
    }

    public void stopBlinking() {
        this.blinking = false;
        this.releasePackets();
    }

    public void releasePackets() {
        if (!this.packetsQueue.isEmpty()) {
            for (Packet p : this.packetsQueue) {
                mc.getNetHandler().getNetworkManager().sendPacketFinal(p);
            }
            this.packetsQueue.clear();
        }
    }

    public void releaseWithPingPacketsFirst() {
        if (!this.packetsQueue.isEmpty()) {
            for (Packet p : this.packetsQueue) {
                if (!(p instanceof C0FPacketConfirmTransaction) && !(p instanceof C00PacketKeepAlive)) continue;
                mc.getNetHandler().getNetworkManager().sendPacketFinal(p);
            }
            for (Packet p : this.packetsQueue) {
                if (p instanceof C0FPacketConfirmTransaction || p instanceof C00PacketKeepAlive) continue;
                mc.getNetHandler().getNetworkManager().sendPacketFinal(p);
            }
            this.packetsQueue.clear();
        }
    }

    public void releaseWithPingPacketsLast() {
        if (!this.packetsQueue.isEmpty()) {
            for (Packet p : this.packetsQueue) {
                if (p instanceof C0FPacketConfirmTransaction || p instanceof C00PacketKeepAlive) continue;
                mc.getNetHandler().getNetworkManager().sendPacketFinal(p);
            }
            for (Packet p : this.packetsQueue) {
                if (!(p instanceof C0FPacketConfirmTransaction) && !(p instanceof C00PacketKeepAlive)) continue;
                mc.getNetHandler().getNetworkManager().sendPacketFinal(p);
            }
            this.packetsQueue.clear();
        }
    }

    public void releasePingPackets() {
        if (!this.packetsQueue.isEmpty()) {
            ArrayList<Packet> toRemove = new ArrayList<Packet>();
            for (Packet p : this.packetsQueue) {
                if (!(p instanceof C0FPacketConfirmTransaction) && !(p instanceof C00PacketKeepAlive)) continue;
                mc.getNetHandler().getNetworkManager().sendPacketFinal(p);
                toRemove.add(p);
            }
            for (Packet p : toRemove) {
                this.packetsQueue.remove(p);
            }
            toRemove.clear();
        }
    }

    public void clearPackets() {
        this.packetsQueue.clear();
    }

    public boolean isBlinking() {
        return this.blinking;
    }
}

