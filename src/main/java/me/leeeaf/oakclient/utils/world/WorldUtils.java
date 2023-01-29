package me.leeeaf.oakclient.utils.world;

import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.List;

import static me.leeeaf.oakclient.OakClient.mc;

public class WorldUtils {
    public static List<WorldChunk> getLoadedChunks(){
        List<WorldChunk> chunks = new ArrayList<>();
        Integer renderDistance = mc.options.getViewDistance().getValue();
        for (int chunkX = -renderDistance; chunkX <= renderDistance; chunkX++) {
            for (int chunkZ = -renderDistance; chunkZ <= renderDistance; chunkZ++) {
                WorldChunk chunk = mc.world.getChunkManager().getWorldChunk((int) mc.player.getX() / 16 + chunkX, (int) mc.player.getZ() / 16 + chunkZ);
                if (chunk != null) {
                    chunks.add(chunk);
                }
            }
        }
        return chunks;
    }
}
