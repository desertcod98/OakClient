package me.leeeaf.oakclient.systems.social;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class SocialManager {
    private static Map<String, Relationship> relationshipMap = new HashMap<>();

    public static void addRelationship(String playerName, Relationship relationship) throws PlayerNotFoundException{
        for(PlayerEntity playerEntity: mc.world.getPlayers()){
            if(playerEntity.getEntityName().equalsIgnoreCase(playerName) && !playerEntity.equals(mc.player)){
                relationshipMap.put(playerName, relationship);
                return;
            }
        }
        throw new PlayerNotFoundException(playerName);
    }
}
