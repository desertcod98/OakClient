package me.leeeaf.oakclient.event.events.packets;

import me.leeeaf.oakclient.event.Event;
import net.minecraft.network.Packet;

public class PacketSendEvent extends Event {
    public Packet<?> packet;
    public PacketSendEvent(Packet<?> packet){
        this.packet = packet;
    }
}
