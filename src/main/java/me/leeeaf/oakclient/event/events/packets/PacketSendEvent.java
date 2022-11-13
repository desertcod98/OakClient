package me.leeeaf.oakclient.event.events.packets;

import me.leeeaf.oakclient.event.Cancellable;
import net.minecraft.network.Packet;

public class PacketSendEvent extends Cancellable {
    public Packet<?> packet;
    public PacketSendEvent(Packet<?> packet){
        this.packet = packet;
    }
}
