package me.leeeaf.oakclient.systems.modules.player;

import me.leeeaf.oakclient.event.EventSubscribe;
import me.leeeaf.oakclient.event.events.PacketEvent;
import me.leeeaf.oakclient.gui.setting.BooleanSetting;
import me.leeeaf.oakclient.gui.setting.IntegerSetting;
import me.leeeaf.oakclient.mixin.packets.EntityVelocityUpdateS2CPacketAccessor;
import me.leeeaf.oakclient.mixin.packets.ExplosionS2CPacketAccessor;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class AntiKnockback extends Module{
    private final IntegerSetting velXZ_setting= new IntegerSetting("XZ velocity", "XZvelocity", "Horizontal velocity to keep", ()->true, 0, 100, 0);
    private final IntegerSetting velY_setting= new IntegerSetting("Y velocity", "Yvelocity", "Vertical velocity to keep", ()->true, 0, 100, 0);

    private final BooleanSetting explosions = new BooleanSetting("Explosions", "explosions", "Reduces knockback from explosions", ()->true, true);
    private final IntegerSetting explosions_velXZ_setting= new IntegerSetting("XZ velocity", "XZvelocity", "Horizontal velocity to keep", ()->true, 0, 100, 0);
    private final IntegerSetting explosions_velY_setting= new IntegerSetting("Y velocity", "Yvelocity", "Vertical velocity to keep", ()->true, 0, 100, 0);


    public AntiKnockback() {
        super("AntiKnockback", "Reduces knockback from various sources", ()->true, true, Category.PLAYER);
        settings.add(velXZ_setting);
        settings.add(velY_setting);
        settings.add(explosions);
        explosions.subSettings.add(explosions_velXZ_setting);
        explosions.subSettings.add(explosions_velY_setting);
    }

    @EventSubscribe
    public void onPacketRecieve(PacketEvent.Receive event) {
        if (mc.player == null)
            return;

        if (event.packet instanceof EntityVelocityUpdateS2CPacket packet) {
            if (packet.getId() == mc.player.getId()) {
                double velXZ = velXZ_setting.getValue()/100d;
                double velY = velY_setting.getValue()/100d;

                double pvelX = (packet.getVelocityX() / 8000d - mc.player.getVelocity().x) * velXZ;
                double pvelY = (packet.getVelocityY() / 8000d - mc.player.getVelocity().y) * velY;
                double pvelZ = (packet.getVelocityZ() / 8000d - mc.player.getVelocity().z) * velXZ;

                ((EntityVelocityUpdateS2CPacketAccessor) packet).setX((int) (pvelX * 8000 + mc.player.getVelocity().x * 8000));
                ((EntityVelocityUpdateS2CPacketAccessor) packet).setY((int) (pvelY * 8000 + mc.player.getVelocity().y * 8000));
                ((EntityVelocityUpdateS2CPacketAccessor) packet).setZ((int) (pvelZ * 8000 + mc.player.getVelocity().z * 8000));
            }
        }else if(event.packet instanceof ExplosionS2CPacket packet && explosions.getValue()){
            double velXZ = explosions_velXZ_setting.getValue()/100d;
            double velY = explosions_velY_setting.getValue()/100d;

            ((ExplosionS2CPacketAccessor) packet).setPlayerVelocityX((float) (packet.getPlayerVelocityX()*velXZ));
            ((ExplosionS2CPacketAccessor) packet).setPlayerVelocityZ((float) (packet.getPlayerVelocityZ()*velXZ));
            ((ExplosionS2CPacketAccessor) packet).setPlayerVelocityY((float) (packet.getPlayerVelocityY()*velY));
        }
    }
}
