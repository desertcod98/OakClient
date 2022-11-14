package me.leeeaf.oakclient.event.events.packets;

import me.leeeaf.oakclient.event.Cancellable;
import net.minecraft.network.Packet;

public class PacketRecieveEvent extends Cancellable {
    public Packet<?> packet;
    public PacketRecieveEvent(Packet<?> packet){
        this.packet = packet;
    }
}
