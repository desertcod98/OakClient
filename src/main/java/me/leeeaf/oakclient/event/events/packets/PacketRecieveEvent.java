package me.leeeaf.oakclient.event.events.packets;

import me.leeeaf.oakclient.event.Event;
import net.minecraft.network.Packet;

public class PacketRecieveEvent extends Event {
    public Packet<?> packet;
    public PacketRecieveEvent(Packet<?> packet){
        this.packet = packet;
    }
}
