package me.leeeaf.oakclient.event.events;

import me.leeeaf.oakclient.event.Event;
import net.minecraft.network.Packet;

public class PacketEvent extends Event {
    public Packet<?> packet;

    public PacketEvent(Packet<?> packet){
        this.packet = packet;
    }

    public static class Send extends PacketEvent{
        public Send(Packet<?> packet){
           super(packet);
        }
    }

    public static class Receive extends PacketEvent{
        public Receive(Packet<?> packet){
            super(packet);
        }
    }
}
