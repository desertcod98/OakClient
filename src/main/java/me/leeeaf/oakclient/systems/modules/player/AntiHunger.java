package me.leeeaf.oakclient.systems.modules.player;

import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.IEventListener;
import me.leeeaf.oakclient.event.events.packets.PacketSendEvent;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.systems.modules.Category;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class AntiHunger extends Module implements IEventListener {
    public AntiHunger() {
        super("AntiHunger", "REDUCES hunger consumption", ()->true, true, Category.PLAYER);
    }

    @Override
    public void onDisable() {
        EventBus.getEventBus().unsubscribe(this);
    }

    @Override
    public void onEnable() {
        EventBus.getEventBus().subscribe(this);
    }


    @Override
    public void call(Object event) {
        if (((PacketSendEvent) event).packet instanceof ClientCommandC2SPacket ) {
            ClientCommandC2SPacket.Mode mode = ((ClientCommandC2SPacket) ((PacketSendEvent) event).packet).getMode();

            if (mode == ClientCommandC2SPacket.Mode.START_SPRINTING || mode == ClientCommandC2SPacket.Mode.STOP_SPRINTING) {
                ((PacketSendEvent) event).cancel();
            }
        }
    }

    @Override
    public Class<?>[] getTargets() {
        return new Class[]{PacketSendEvent.class};
    }
}
