package me.leeeaf.oakclient.systems.modules.render;

import com.google.common.collect.Sets;
import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.IEventListener;
import me.leeeaf.oakclient.event.WorldRenderEvent;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.systems.renderer.Renderer;
import me.leeeaf.oakclient.systems.renderer.color.LineColor;
import me.leeeaf.oakclient.utils.world.ChunkProcessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class Search extends Module implements IEventListener {
    public static Set<Block> searchTargets = new HashSet<>();
    private Set<BlockPos> foundBlocks = Sets.newConcurrentHashSet();

    private ChunkProcessor chunkProcessor = new ChunkProcessor(
            1, //threads
            (chunk) -> { //loadChunkConsumer
                ChunkPos chunkPos = chunk.getPos();
                for(int x = 0; x<16; x++) {
                    for (int y = mc.world.getBottomY(); y < mc.world.getTopY(); y++) {
                        for (int z = 0; z < 16; z++) {
                            BlockPos pos = new BlockPos(chunkPos.getStartX() + x, y, chunkPos.getStartZ() + z);
                            BlockState state = chunk.getBlockState(pos);
                            if(searchTargets.contains(state.getBlock())){
                                foundBlocks.add(pos);
                            }
                        }
                    }
                }
            },
            (blockPos, blockState) -> { //updateBlockConsumer
                if(searchTargets.contains(blockState.getBlock())){
                    foundBlocks.add(blockPos); //todo does it work?
                }
            }
    );

    private Set<Block> prevSearchTargets = new HashSet<>();
    private int prevRenderDistance = -1; //initialize

    public Search() {
        super("Search", "Searchs  for specified blocks (command .search)", ()->true, true, Category.RENDER);
    }

    @Override
    public void onEnable(){
        EventBus.getEventBus().subscribe(this);
        chunkProcessor.start();
    }

    @Override
    public void onDisable(){
        EventBus.getEventBus().unsubscribe(this);
        chunkProcessor.stop();
        foundBlocks.clear();
    }

    @Override
    public void onTick(){
        if(!prevSearchTargets.equals(searchTargets) || prevRenderDistance!=mc.options.getViewDistance().getValue()){
            foundBlocks.clear();
            chunkProcessor.processAllLoadedChunks();
            prevSearchTargets = searchTargets;
            prevRenderDistance = mc.options.getViewDistance().getValue();
        }
    }


    @Override
    public void call(Object event) {
        if(event instanceof WorldRenderEvent){
            Vec3d camVec = new Vec3d(0, 0, 75)
                    .rotateX(-(float) Math.toRadians(mc.gameRenderer.getCamera().getPitch()))
                    .rotateY(-(float) Math.toRadians(mc.gameRenderer.getCamera().getYaw()))
                    .add(mc.cameraEntity.getEyePos());
            for(BlockPos blockPos : foundBlocks){
                LineColor lineColor =  LineColor.single(Color.PINK.getRed(), Color.PINK.getGreen(), Color.PINK.getBlue(), 0.75f);
                Renderer.drawLine(camVec.x, camVec.y, camVec.z, blockPos.getX(), blockPos.getY(), blockPos.getZ(), lineColor, 1);
            }
        }
    }

    @Override
    public Class<?>[] getTargets() {
        return new Class[]{WorldRenderEvent.Post.class};
    }
}
