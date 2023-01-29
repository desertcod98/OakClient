package me.leeeaf.oakclient.systems.modules.player;

import me.leeeaf.oakclient.event.EventSubscribe;
import me.leeeaf.oakclient.event.events.PacketEvent;
import me.leeeaf.oakclient.gui.setting.BooleanSetting;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

import static me.leeeaf.oakclient.OakClient.mc;

public class AntiHunger extends Module {
    private final BooleanSetting pauseInLiquids = new BooleanSetting("Pause in liquids", "pauseInLiquids", "Pause module in liquids", ()->true, true);

    public AntiHunger() {
        super("AntiHunger", "REDUCES hunger consumption", ()->true, true, Category.PLAYER);
        settings.add(pauseInLiquids);
    }

    @EventSubscribe
    public void onPacketSend(PacketEvent.Send event) {
        if (event.packet instanceof ClientCommandC2SPacket) {
            if(pauseInLiquids.getValue() && (mc.player.isTouchingWater() || mc.player.isInLava())){
                return;
            }
            ClientCommandC2SPacket.Mode mode = ((ClientCommandC2SPacket) (event).packet).getMode();

            if (mode == ClientCommandC2SPacket.Mode.START_SPRINTING || mode == ClientCommandC2SPacket.Mode.STOP_SPRINTING) {
                event.cancel();
            }
        }
    }
}
