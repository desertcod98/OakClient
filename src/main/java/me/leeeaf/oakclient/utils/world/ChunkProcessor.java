package me.leeeaf.oakclient.utils.world;

import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.EventSubscribe;
import me.leeeaf.oakclient.event.events.PacketEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class ChunkProcessor {
    private ExecutorService executor;

    private int threads;
    private Consumer<WorldChunk> loadChunkConsumer;
    private BiConsumer<BlockPos, BlockState> updateBlockConsumer;

    public ChunkProcessor(int threads, Consumer<WorldChunk> loadChunkConsumer, BiConsumer<BlockPos, BlockState> updateBlockConsumer) {
        this.threads = threads;
        this.loadChunkConsumer = loadChunkConsumer;
        this.updateBlockConsumer = updateBlockConsumer;
    }

    public void start(){
        executor = Executors.newFixedThreadPool(threads);
        EventBus.getEventBus().subscribe(this);
    }

    public void stop(){
        executor.shutdownNow();
        executor = null;
        EventBus.getEventBus().unsubscribe(this);
    }

    public void processAllLoadedChunks(){
        if(loadChunkConsumer!=null){
            for(WorldChunk chunk: WorldUtils.getLoadedChunks()){
                executor.execute(()->loadChunkConsumer.accept(chunk));
            }
        }
    }

    @EventSubscribe
    public void onPacketRecieve(PacketEvent.Receive event) {
        if(mc.world ==null){
            return;
        }

        Packet<?> packet = event.packet;

        if(updateBlockConsumer!=null && packet instanceof BlockUpdateS2CPacket){
            executor.execute(()->updateBlockConsumer.accept(((BlockUpdateS2CPacket) packet).getPos(), ((BlockUpdateS2CPacket) packet).getState()));
        }else if (updateBlockConsumer != null && packet instanceof ExplosionS2CPacket) {

            for (BlockPos pos: ((ExplosionS2CPacket) packet).getAffectedBlocks()) {
                executor.execute(() -> updateBlockConsumer.accept(pos, Blocks.AIR.getDefaultState()));
            }
        } else if (updateBlockConsumer != null && packet instanceof ChunkDeltaUpdateS2CPacket) {

            ((ChunkDeltaUpdateS2CPacket) packet).visitUpdates((pos, state) -> {
                BlockPos immutablePos = pos.toImmutable();
                executor.execute(() -> updateBlockConsumer.accept(immutablePos, state));
            });
        } else if (loadChunkConsumer != null && packet instanceof ChunkDataS2CPacket) {
            ChunkPos cp = new ChunkPos(((ChunkDataS2CPacket) packet).getX(), ((ChunkDataS2CPacket) packet).getZ());
            WorldChunk chunk = new WorldChunk(mc.world, cp);
            chunk.loadFromPacket(((ChunkDataS2CPacket) packet).getChunkData().getSectionsDataBuf(), new NbtCompound(), ((ChunkDataS2CPacket) packet).getChunkData().getBlockEntities(((ChunkDataS2CPacket) packet).getX(), ((ChunkDataS2CPacket) packet).getZ()));
            executor.execute(() -> loadChunkConsumer.accept(chunk));
        }
    }
}
