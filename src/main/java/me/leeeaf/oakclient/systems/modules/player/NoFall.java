package me.leeeaf.oakclient.systems.modules.player;

import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.EventSubscribe;
import me.leeeaf.oakclient.event.events.PacketEvent;
import me.leeeaf.oakclient.mixin.packets.PlayerMoveC2SPacketAccessor;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
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

    @EventSubscribe
    public void onPacketSend(PacketEvent.Send event) {
        if(event.packet instanceof PlayerMoveC2SPacket){
            ((PlayerMoveC2SPacketAccessor) event.packet).setOnGround(true);
        }

    }
}
