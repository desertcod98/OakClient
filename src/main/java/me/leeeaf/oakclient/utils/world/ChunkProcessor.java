package me.leeeaf.oakclient.utils.world;

import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.IEventListener;
import me.leeeaf.oakclient.event.events.packets.PacketRecieveEvent;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ChunkProcessor implements IEventListener {
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

    @Override
    public void call(Object event) {
        if(MinecraftClient.getInstance().world ==null){
            return;
        }

        if(updateBlockConsumer!=null && ((PacketRecieveEvent)event).packet instanceof BlockUpdateS2CPacket){
            BlockUpdateS2CPacket packet = (BlockUpdateS2CPacket) ((PacketRecieveEvent)event).packet;
            executor.execute(()->updateBlockConsumer.accept(packet.getPos(),packet.getState()));
        }
    }

    @Override
    public Class<?>[] getTargets() {
        return new Class[]{PacketRecieveEvent.class};
    }
}
