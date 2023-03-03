package me.leeeaf.oakclient.utils;


import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

import static me.leeeaf.oakclient.OakClient.mc;

public class FakePlayer extends OtherClientPlayerEntity {
    //TODO copy player proprieties (skin ecc)
    public FakePlayer(PlayerEntity player){
        super(mc.world, player.getGameProfile(), null);
        copyPositionAndRotation(player);
        setPose(player.getPose());

        //does not seem to work?
        Byte playerModel = player.getDataTracker().get(PlayerEntity.PLAYER_MODEL_PARTS);
        dataTracker.set(PlayerEntity.PLAYER_MODEL_PARTS, playerModel);
        getAttributes().setFrom(player.getAttributes());

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
