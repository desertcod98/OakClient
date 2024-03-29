package me.leeeaf.oakclient.systems.modules.player;

import me.leeeaf.oakclient.event.EventPriority;
import me.leeeaf.oakclient.event.EventSubscribe;
import me.leeeaf.oakclient.event.events.ClientMoveEvent;
import me.leeeaf.oakclient.event.events.PacketEvent;
import me.leeeaf.oakclient.gui.setting.BooleanSetting;
import me.leeeaf.oakclient.gui.setting.IntegerSetting;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.utils.FakePlayer;
import net.minecraft.entity.EntityPose;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

import static me.leeeaf.oakclient.OakClient.mc;

public class Freecam extends Module {
    private final IntegerSetting flySpeed = new IntegerSetting("Fly speed", "flySpeed", "The speed at which you fly", ()->true,1,100,20);
    private final BooleanSetting chunkCulling = new BooleanSetting("Chunk culling", "chunkCulling", "Disable chunk culling (renders every chunk)", ()->true, true);

    public Freecam() {
        super("Freecam", "Allows you to move the camera freely (use fullbright <3)", ()->true, true, Category.PLAYER);
        settings.add(flySpeed);
        settings.add(chunkCulling);
        //some logic handled in ClientPlayerEntityMixin::onPushOutOfBlocks
    }

    private FakePlayer fakePlayer;
    private Vec3d prevPos;
    private float prevYaw;
    private float prevPitch;
    @Override
    public void onTick() {
        super.onTick();
        mc.player.setOnGround(false);
        mc.player.getAbilities().setFlySpeed(flySpeed.getValue()*0.0015f);
        mc.player.getAbilities().flying = true;
        mc.player.setPose(EntityPose.STANDING);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        prevPos = mc.player.getPos();
        prevPitch = mc.player.getPitch();
        prevYaw = mc.player.getYaw();
        fakePlayer = new FakePlayer(mc.player);
        fakePlayer.spawn();
        mc.chunkCullingEnabled = chunkCulling.isOn();
        mc.worldRenderer.reload();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.player.setYaw(prevYaw);
        mc.player.setPitch(prevPitch);
        mc.player.setPos(prevPos.x, prevPos.y, prevPos.z);
        mc.player.setVelocity(Vec3d.ZERO);
        fakePlayer.despawn();
        mc.chunkCullingEnabled = true;
        mc.worldRenderer.reload();
        mc.player.getAbilities().flying = false;
    }

    @EventSubscribe
    public void onClientMove(ClientMoveEvent event) {
        mc.player.noClip = true;
    }

    @EventSubscribe(priority = EventPriority.HIGH)
    public void onPacketSend(PacketEvent.Send event){
        if(event.packet instanceof PlayerMoveC2SPacket || event.packet instanceof ClientCommandC2SPacket){
            event.cancel();
        }
    }
}
