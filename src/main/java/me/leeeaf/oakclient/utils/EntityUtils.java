package me.leeeaf.oakclient.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;

import static me.leeeaf.oakclient.OakClientClient.mc;
public class EntityUtils {
    public static boolean isAttackable(Entity e) {
        return (e instanceof LivingEntity || e instanceof ShulkerBulletEntity || e instanceof AbstractFireballEntity)
                && e.isAlive()
                && e != mc.player
                && !e.isConnectedThroughVehicle(mc.player);
    }
}
