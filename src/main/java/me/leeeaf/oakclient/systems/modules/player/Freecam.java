package me.leeeaf.oakclient.systems.modules.player;

import me.leeeaf.oakclient.event.EventListener;
import me.leeeaf.oakclient.event.events.ClientMoveEvent;
import me.leeeaf.oakclient.event.events.packets.PacketSendEvent;
import me.leeeaf.oakclient.gui.setting.IntegerSetting;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.utils.FakePlayer;
import net.minecraft.entity.EntityPose;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class Freecam extends Module {
    public final IntegerSetting flySpeed = new IntegerSetting("Fly speed", "FlySpeed", "The speed at which you fly", ()->true,1,100,20);

    public Freecam() {
        super("Freecam", "Allows you to move the camera freely", ()->true, true, Category.PLAYER);
        //todo make this shit better
        //todo cant go through blocks horizontally????? (also all buggy, maybe event system too slow? hope not :') )
    }

    FakePlayer fakePlayer;

    @Override
    public void onTick() {
        super.onTick();
        mc.player.setOnGround(false);
        mc.player.getAbilities().setFlySpeed(flySpeed.getValue()    *0.0015f);
        mc.player.getAbilities().flying = true;
        mc.player.setPose(EntityPose.STANDING);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        fakePlayer = new FakePlayer(mc.player, mc.player.getEntityName());
        fakePlayer.spawn(); //todo first time enabling the module model does not spawn
    }

    @Override
    public void onDisable() {
        super.onDisable();
        fakePlayer.despawn();
    }

    @EventListener
    public void onClientMove(ClientMoveEvent event) {
        mc.player.noClip = true;
    }

    @EventListener
    public void onPacketSend(PacketSendEvent event){
        if(event.packet instanceof PlayerMoveC2SPacket || event.packet instanceof ClientCommandC2SPacket){
            event.cancel();
        }
    }
}
