package me.leeeaf.oakclient.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static me.leeeaf.oakclient.OakClient.mc;
public class EntityUtils {
    public static boolean isAttackable(Entity e) {
        return (e instanceof LivingEntity || e instanceof ShulkerBulletEntity || e instanceof AbstractFireballEntity)
                && e.isAlive()
                && e != mc.player
                && !e.isConnectedThroughVehicle(mc.player);
    }

    public static List<Entity> getEntities(Predicate<Entity> filterPredicate, Comparator<Entity> sortComparator){
        Stream<Entity> players = StreamSupport.stream(mc.world.getEntities().spliterator(), false);
        return players.filter(filterPredicate).sorted(sortComparator).collect(Collectors.toList());
    }

    public static <T> List<T> getMappedEntities(Predicate<Entity> filterPredicate, Comparator<Entity> sortComparator, Function<Entity, T> mappingFunction){
        Stream<Entity> entities = StreamSupport.stream(mc.world.getEntities().spliterator(), false);
        return entities.filter(filterPredicate).sorted(sortComparator).map(mappingFunction).collect(Collectors.toList());
    }

    public static List<PlayerEntity> getPlayers(double range, SortMethod sortMethod){
        Stream<Entity> targets = StreamSupport.stream(mc.world.getEntities().spliterator(), false);
        Comparator<Entity> comparator = null;
        switch(sortMethod){
            case HEALTH -> comparator = Comparator.comparing((entity)-> ((LivingEntity) entity).getHealth());
            case DISTANCE -> comparator = Comparator.comparing(mc.player::distanceTo);
        }
        return targets
                .filter(
                        entity -> entity instanceof PlayerEntity
                        && entity != mc.player
                        && !(((PlayerEntity) entity).isDead() || ((PlayerEntity) entity).getHealth() <= 0)
                        && mc.player.distanceTo(entity) <= range
                )
                .sorted(comparator)
                .map(PlayerEntity.class::cast)
                .collect(Collectors.toList());
    }

    public enum SortMethod{
        DISTANCE,
        HEALTH
    }
}



