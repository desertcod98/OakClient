package me.leeeaf.oakclient.systems.modules.player;

import me.leeeaf.oakclient.event.EventSubscribe;
import me.leeeaf.oakclient.event.events.PacketEvent;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class IgnoreServerPosition extends Module {
    public IgnoreServerPosition() {
        //TODO useless, if server position is ignored new packets are probably rejected? (to test this use this with blink module)
        super("Ignore server position", "Ignores server's position packets", ()->true, true, Category.PLAYER);
    }

    @EventSubscribe
    public void onPacketRecieve(PacketEvent.Receive event){
        if(event.packet instanceof PlayerPositionLookS2CPacket){
            event.cancel();
        }
    }
}
