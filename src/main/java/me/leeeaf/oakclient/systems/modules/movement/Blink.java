package me.leeeaf.oakclient.systems.modules.movement;

import me.leeeaf.oakclient.event.EventSubscribe;
import me.leeeaf.oakclient.event.events.PacketEvent;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.utils.FakePlayer;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.ArrayList;
import java.util.List;

import static me.leeeaf.oakclient.OakClient.mc;

public class Blink extends Module{
    private FakePlayer fakePlayer;


    private List<PlayerMoveC2SPacket> moveC2SPackets;

    public Blink() {
        super("Blink", "Suspends sending of movement packets", ()->true, true, Category.MOVEMENT);
        moveC2SPackets = new ArrayList<>();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        fakePlayer = new FakePlayer(mc.player);
        fakePlayer.spawn();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        fakePlayer.despawn();
        sendMoveC2SPackets();
    }

    @EventSubscribe
    public void onPacketSend(PacketEvent.Send event) {
        if(event.packet instanceof PlayerMoveC2SPacket packet){
            event.cancel();
            moveC2SPackets.add(packet);
        }
    }

    private void sendMoveC2SPackets(){
        moveC2SPackets.forEach(mc.getNetworkHandler()::sendPacket);
        moveC2SPackets.clear();
    }
}
