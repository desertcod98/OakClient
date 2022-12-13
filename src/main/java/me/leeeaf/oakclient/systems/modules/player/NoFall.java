package me.leeeaf.oakclient.systems.modules.player;

import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.EventListener;
import me.leeeaf.oakclient.event.IEventListener;
import me.leeeaf.oakclient.event.events.packets.PacketSendEvent;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.mixin.packets.PlayerMoveC2SPacketAccessor;
import me.leeeaf.oakclient.systems.modules.Category;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module{
    public NoFall() {
        super("NoFall", "Prevents you from taking fall damage", ()->true, true, Category.PLAYER);
    }

    @Override
    public void onDisable() {
        EventBus.getEventBus().unsubscribe(this);
    }

    @Override
    public void onEnable() {
        EventBus.getEventBus().subscribe(this);
    }

    @EventListener
    public void onPacketSend(PacketSendEvent event) {
        if(((PacketSendEvent) event).packet instanceof PlayerMoveC2SPacket){
            ((PlayerMoveC2SPacketAccessor) ((PacketSendEvent) event).packet).setOnGround(true);
        }

    }
}
