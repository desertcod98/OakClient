package me.leeeaf.oakclient.systems.modules.combat;

import me.leeeaf.oakclient.event.EventListener;
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

public class Criticals extends Module{
    public Criticals() {
        super("Criticals", "Tries to make every hit on entities critical", ()->true, true, Category.COMBAT);
        //code that needs to be triggered (class PlayerEntity):
        //boolean bl3 = bl && this.fallDistance > 0.0F && !this.onGround && !this.isClimbing() && !this.isTouchingWater() && !this.hasStatusEffect(StatusEffects.BLINDNESS) && !this.hasVehicle() && target instanceof LivingEntity;
    }

    @EventListener
    public void onPacketSend(PacketSendEvent event) {
        if (event.packet instanceof PlayerInteractEntityC2SPacket packet) {
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
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.07, z, false));
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false));

        }
        if (sprinting) {
            mc.player.setSprinting(true);
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
        }
    }
}
