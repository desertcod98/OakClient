package me.leeeaf.oakclient.systems.modules.world;

import com.google.gson.JsonObject;
import me.leeeaf.oakclient.event.EventSubscribe;
import me.leeeaf.oakclient.event.events.render.RenderEntityLabelEvent;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.systems.renderer.text.TextRenderer;
import me.leeeaf.oakclient.utils.net.Executor;
import me.leeeaf.oakclient.utils.net.HttpManger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static me.leeeaf.oakclient.OakClient.mc;

public class EntityOwner extends Module {

    public EntityOwner() {
        //TODO make tag color an option
        super("EntityOwner", "Shows entities owners usernames", ()->true, true, Category.WORLD);
        //some logic handled in EntityRendererMixin::(ModifyArgs)renderLabelIfPresent
        //and in EntityRendererMixin::hasLabel
    }

    private final Map<UUID, String> playerUUIDToUsername = new HashMap<>();

    @EventSubscribe
    public void onRenderEntityLabel(RenderEntityLabelEvent event){
        Entity entity = event.entity;
        UUID ownerUUID = null;
        if(entity instanceof TameableEntity tameableEntity) ownerUUID = tameableEntity.getOwnerUuid();
        else if(entity instanceof AbstractHorseEntity abstractHorseEntity) ownerUUID = abstractHorseEntity.getOwnerUuid();
        if(ownerUUID != null){
            //if Mojang response in resolvePlayerUUID is null, we are going to send a request every tick which is useless,
            //so we set the String in the hashmap to "Failed to resolve username" (because we can't set it to null, as
            //we wouldn't have a way to check if the UUID hasn't been resolved yet or if the Mojang response was null.
            //We are forced to use a String like this because the hashmap is <UUID, String>
            if(!Objects.equals(playerUUIDToUsername.get(ownerUUID), "Failed to resolve username")) {
                String ownerUsername = resolvePlayerUUID(ownerUUID);
                if (ownerUsername != null) {
                    TextRenderer.drawLabelOnEntity(ownerUsername, Formatting.YELLOW, 1F, entity, event.matrices, event.light, event.vertexConsumers, event.dispatcher);
                }
            }
        }

    }

    private String resolvePlayerUUID(UUID playerUUID){
        //If player is online
        PlayerEntity playerEntity = mc.world.getPlayerByUuid(playerUUID);
        if (playerEntity != null) return playerEntity.getEntityName();

        //If username is saved in cache (already requested to Mojang servers)
        String username = playerUUIDToUsername.get(playerUUID);
        if(username!=null) return username;

        //Else make a request to Mojang servers (doesn't work in cracked servers)
        Executor.execute(()->{
            playerUUIDToUsername.put(playerUUID, "Loading...");
            JsonObject response = HttpManger.GETJson("https://sessionserver.mojang.com/session/minecraft/profile/"
                    +playerUUID.toString().replace("-", ""));
            if(response!=null) playerUUIDToUsername.put(playerUUID, response.get("name").getAsString());
            else playerUUIDToUsername.put(playerUUID, "Failed to resolve username");
        });

        return null;
    }
}
