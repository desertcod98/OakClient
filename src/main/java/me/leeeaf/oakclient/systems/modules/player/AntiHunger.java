package me.leeeaf.oakclient.systems.modules.player;

import me.leeeaf.oakclient.event.EventSubscribe;
import me.leeeaf.oakclient.event.events.PacketEvent;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class AntiHunger extends Module {
    public AntiHunger() {
        //TODO if you are in liquids in servers it creates problems
        super("AntiHunger", "REDUCES hunger consumption", ()->true, true, Category.PLAYER);
    }

    @EventSubscribe
    public void onPacketSend(PacketEvent.Send event) {
        if (event.packet instanceof ClientCommandC2SPacket ) {
            ClientCommandC2SPacket.Mode mode = ((ClientCommandC2SPacket) (event).packet).getMode();

            if (mode == ClientCommandC2SPacket.Mode.START_SPRINTING || mode == ClientCommandC2SPacket.Mode.STOP_SPRINTING) {
                event.cancel();
            }
        }
    }
}
