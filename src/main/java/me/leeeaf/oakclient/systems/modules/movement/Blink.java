package me.leeeaf.oakclient.systems.modules.movement;

import me.leeeaf.oakclient.event.EventSubscribe;
import me.leeeaf.oakclient.event.events.PacketEvent;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.utils.FakePlayer;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class Blink extends Module{
    private FakePlayer fakePlayer;

    public Blink() {
        super("Blink", "Suspends sending of movement packets", ()->true, true, Category.MOVEMENT);
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
    }

    @EventSubscribe
    public void onPacketSend(PacketEvent.Send event) {
        if(event.packet instanceof PlayerMoveC2SPacket){
            event.cancel();
        }
    }
}
