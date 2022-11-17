package me.leeeaf.oakclient.systems.modules.combat;

import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.IEventListener;
import me.leeeaf.oakclient.event.events.packets.PacketSendEvent;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.utils.PlayerInteractEntityC2SUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class Criticals extends Module implements IEventListener {
    public Criticals() {
        super("Criticals", "Tries to make every hit on entities critical", ()->true, true, Category.COMBAT); //TODO should study how it works better

    }

    @Override
    public void call(Object event) {
        if (((PacketSendEvent) event).packet instanceof PlayerInteractEntityC2SPacket packet) {
            if (PlayerInteractEntityC2SUtils.getInteractType(packet) == PlayerInteractEntityC2SUtils.InteractType.ATTACK
                    && PlayerInteractEntityC2SUtils.getEntity(packet) instanceof LivingEntity) {
                sendCritPackets();
            }
        }
    }

    private void sendCritPackets() {
        if (mc.player.isClimbing() || mc.player.isTouchingWater()
                || mc.player.hasStatusEffect(StatusEffects.BLINDNESS) || mc.player.hasVehicle()) {
            return;
        }

        boolean sprinting = mc.player.isSprinting();
        if (sprinting) {
            mc.player.setSprinting(false);
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
        }

        if (mc.player.isOnGround()) {
            double x = mc.player.getX();
            double y = mc.player.getY();
            double z = mc.player.getZ();
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.0633, z, false));
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false));

        }
        if (sprinting) {
            mc.player.setSprinting(true);
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
        }
    }

    @Override
    public Class<?> getTarget() {
        return PacketSendEvent.class;
    }

    @Override
    public void onDisable() {
        EventBus.getEventBus().unsubscribe(this);
    }

    @Override
    public void onEnable() {
        EventBus.getEventBus().subscribe(this);
    }

    @Override
    public void onTick() {

    }
}
