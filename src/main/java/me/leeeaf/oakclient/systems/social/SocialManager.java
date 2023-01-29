package me.leeeaf.oakclient.systems.social;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

import static me.leeeaf.oakclient.OakClient.mc;

public class SocialManager {
    private static final Map<String, Relationship> relationshipMap = new HashMap<>();

    public static void addRelationship(String playerName, Relationship relationship) throws PlayerNotFoundException{
        for(PlayerEntity playerEntity: mc.world.getPlayers()){
            if(playerEntity.getEntityName().equalsIgnoreCase(playerName) && !playerEntity.getEntityName().equalsIgnoreCase(mc.player.getEntityName())){
                relationshipMap.put(playerName, relationship);
                return;
            }
        }
        throw new PlayerNotFoundException(playerName);
    }

    public static void removeRelationship(String playerName) throws PlayerNotFoundException{
        for(PlayerEntity playerEntity: mc.world.getPlayers()){
            if(playerEntity.getEntityName().equalsIgnoreCase(playerName)){
                relationshipMap.remove(playerName);
                return;
            }
        }
        throw new PlayerNotFoundException(playerName);
    }

    public static Relationship getRelationship(String playerName){
        return relationshipMap.get(playerName);
    }
}
