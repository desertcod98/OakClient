package me.leeeaf.oakclient.systems.modules.player;

import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.EventListener;
import me.leeeaf.oakclient.event.IEventListener;
import me.leeeaf.oakclient.event.events.packets.PacketSendEvent;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.systems.modules.Category;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class AntiHunger extends Module {
    public AntiHunger() {
        super("AntiHunger", "REDUCES hunger consumption", ()->true, true, Category.PLAYER);
    }

    @EventListener
    public void onPacketSend(PacketSendEvent event) {
        if (event.packet instanceof ClientCommandC2SPacket ) {
            ClientCommandC2SPacket.Mode mode = ((ClientCommandC2SPacket) (event).packet).getMode();

            if (mode == ClientCommandC2SPacket.Mode.START_SPRINTING || mode == ClientCommandC2SPacket.Mode.STOP_SPRINTING) {
                event.cancel();
            }
        }
    }
}
