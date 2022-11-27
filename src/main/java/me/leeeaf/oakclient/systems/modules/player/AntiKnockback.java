package me.leeeaf.oakclient.systems.modules.player;

import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.IEventListener;
import me.leeeaf.oakclient.event.events.packets.PacketRecieveEvent;
import me.leeeaf.oakclient.gui.setting.IntegerSetting;
import me.leeeaf.oakclient.mixin.packets.EntityVelocityUpdateS2CPacketAccessor;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class AntiKnockback extends Module implements IEventListener {
    private final IntegerSetting velXZ_setting= new IntegerSetting("XZ velocity", "XZvelocity", "Horizontal velocity to keep", ()->true, 0, 100, 0);
    private final IntegerSetting velY_setting= new IntegerSetting("Y velocity", "Yvelocity", "Vertical velocity to keep", ()->true, 0, 100, 0);

    public AntiKnockback() {
        super("AntiKnockback", "Reduces entity damage knockback", ()->true, true, Category.PLAYER);
        settings.add(velXZ_setting);
        settings.add(velY_setting);
    }

    @Override
    public void call(Object event) {
        if (mc.player == null)
            return;

        if ((((PacketRecieveEvent) event).packet) instanceof EntityVelocityUpdateS2CPacket packet) {
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
        }
    }

    @Override
    public Class<?>[] getTargets() {
        return new Class[]{PacketRecieveEvent.class};
    }

    @Override
    public void onDisable() {
        EventBus.getEventBus().unsubscribe(this);
    }

    @Override
    public void onEnable() {
        EventBus.getEventBus().subscribe(this);
    }

}
