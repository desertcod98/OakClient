package me.leeeaf.oakclient.systems.modules.player;

import me.leeeaf.oakclient.event.EventListener;
import me.leeeaf.oakclient.event.events.ClientMoveEvent;
import me.leeeaf.oakclient.event.events.packets.PacketSendEvent;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.utils.FakePlayer;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.world.GameMode;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class Freecam extends Module {
    public Freecam() {
        super("Freecam", "Allows you to move the camera freely", ()->true, true, Category.PLAYER);
        //todo make this shit better
    }

    private GameMode prevGamemode;
    FakePlayer fakePlayer;

    @Override
    public void onEnable() {
        super.onEnable();
        prevGamemode = mc.interactionManager.getCurrentGameMode();
        mc.interactionManager.setGameMode(GameMode.SPECTATOR);
        fakePlayer = new FakePlayer(mc.player, mc.player.getEntityName());
        fakePlayer.spawn(); //todo first time enabling the module model does not spawn
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.interactionManager.setGameMode(prevGamemode);
        fakePlayer.despawn();
    }

    @EventListener
    public void onClientMove(ClientMoveEvent event) {
        mc.player.noClip = true;
    }

    @EventListener
    public void onPacketSend(PacketSendEvent event){
        if(event.packet instanceof PlayerMoveC2SPacket){
            event.cancel();
        }
    }
}
