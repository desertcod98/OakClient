package me.leeeaf.oakclient.utils;


import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class FakePlayer extends OtherClientPlayerEntity {
    public FakePlayer(PlayerEntity player){
        super(mc.world, player.getGameProfile(), null);
        copyPositionAndRotation(player);
        setPose(player.getPose());
        setHealth(20);
        setUuid(UUID.randomUUID());
    }
    public void spawn(){
        unsetRemoved();
        mc.world.addEntity(getId(), this);
    }

    public void despawn(){
        mc.world.removeEntity(getId(), RemovalReason.DISCARDED);
        setRemoved(RemovalReason.DISCARDED);
    }
}
