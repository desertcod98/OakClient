package me.leeeaf.oakclient.systems.modules.combat;

import me.leeeaf.oakclient.event.EventSubscribe;
import me.leeeaf.oakclient.event.events.PacketEvent;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.utils.PlayerInteractEntityC2SUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class Criticals extends Module {
    public Criticals() {
        //TODO bypass NCP ? :(
        super("Criticals", "Tries to make every hit on entities critical", () -> true, true, Category.COMBAT);
        //code that needs to be triggered (class PlayerEntity):
        //boolean bl3 = bl && this.fallDistance > 0.0F && !this.onGround && !this.isClimbing() && !this.isTouchingWater() && !this.hasStatusEffect(StatusEffects.BLINDNESS) && !this.hasVehicle() && target instanceof LivingEntity;
    }
    private int critPacketsToSend;
    private LivingEntity entityToAttack;
    private boolean wasSprinting;
    private double[] yOffsets = {0.08, 0.12, 0.02}; //TODO this values sometimes bypass NCP and sometimes trigger fallheight mismatch (and seem to trigger survival fly a lot)
    boolean attacked;

    @Override
    public void onEnable() {
        super.onEnable();
        critPacketsToSend = 0;
    }

    @EventSubscribe
    public void onPacketSend(PacketEvent.Send event) {
        if (event.packet instanceof PlayerInteractEntityC2SPacket packet) {
            if (PlayerInteractEntityC2SUtils.getInteractType(packet) == PlayerInteractEntityC2SUtils.InteractType.ATTACK
                    && PlayerInteractEntityC2SUtils.getEntity(packet) instanceof LivingEntity entity && !attacked) {
                if (!(mc.player.isClimbing() || mc.player.isTouchingWater()
                        || mc.player.hasStatusEffect(StatusEffects.BLINDNESS) || mc.player.hasVehicle())) {
                    entityToAttack = entity;
                    critPacketsToSend = 3;
                    wasSprinting = mc.player.isSprinting();
                    event.cancel();
                }
            } else if (attacked) {
                attacked = false;
            }
        }
    }

    @Override
    public void onTick() {
        //TODO sending too many packets in one tick doesn't work, but sending one each tick is very slow
        super.onTick();
        if (critPacketsToSend > 0) {
            if (wasSprinting && mc.player.isSprinting()) {
                mc.player.setSprinting(false);
                mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
            }

            if (mc.player.isOnGround()) {
                double x = mc.player.getX();
                double y = mc.player.getBoundingBox().minY;
                double z = mc.player.getZ();
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + yOffsets[yOffsets.length - critPacketsToSend], z, false));

            }
            critPacketsToSend--;
            if (critPacketsToSend == 0) {
                if (wasSprinting) {
                    mc.player.setSprinting(true);
                    mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
                }
                mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(entityToAttack, false));
                attacked = true;
            }
        }
    }

}

