package me.leeeaf.oakclient.utils;


import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class FakePlayer extends OtherClientPlayerEntity {
    public FakePlayer(PlayerEntity player, String username){
        super(mc.world, player.getGameProfile(), player.getPublicKey());
        copyPositionAndRotation(player);
        setPose(player.getPose());
        setHealth(20);
    }
    public void spawn(){
        mc.world.addEntity(getId(), this);
    }

    public void despawn(){
        mc.world.removeEntity(getId(), RemovalReason.DISCARDED);
        setRemoved(RemovalReason.DISCARDED);
    }
}
