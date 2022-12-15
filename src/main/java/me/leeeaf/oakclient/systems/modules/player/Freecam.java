package me.leeeaf.oakclient.systems.modules.player;

import me.leeeaf.oakclient.event.EventListener;
import me.leeeaf.oakclient.event.events.ClientMoveEvent;
import me.leeeaf.oakclient.event.events.packets.PacketSendEvent;
import me.leeeaf.oakclient.gui.setting.BooleanSetting;
import me.leeeaf.oakclient.gui.setting.IntegerSetting;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.utils.FakePlayer;
import net.minecraft.entity.EntityPose;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class Freecam extends Module {
    private final IntegerSetting flySpeed = new IntegerSetting("Fly speed", "FlySpeed", "The speed at which you fly", ()->true,1,100,20);
    private final BooleanSetting chunkCulling = new BooleanSetting("Chunk culling", "chunkCulling", "Disable chunk culling (renders every chunk)", ()->true, true);

    public Freecam() {
        super("Freecam", "Allows you to move the camera freely (use fullbright <3)", ()->true, true, Category.PLAYER);
        settings.add(flySpeed);
        settings.add(chunkCulling);
        //some logic handled in ClientPlayerEntityMixin::onPushOutOfBlocks
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
        fakePlayer = new FakePlayer(mc.player);
        fakePlayer.spawn();
        mc.chunkCullingEnabled = chunkCulling.isOn();
        mc.worldRenderer.reload();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        fakePlayer.despawn();
        mc.chunkCullingEnabled = true;
        mc.worldRenderer.reload();
        mc.player.getAbilities().flying = false;
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
