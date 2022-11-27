package me.leeeaf.oakclient.systems.modules.render;

import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.IEventListener;
import me.leeeaf.oakclient.event.WorldRenderEvent;
import me.leeeaf.oakclient.gui.setting.BooleanSetting;
import me.leeeaf.oakclient.gui.setting.ColorSetting;
import me.leeeaf.oakclient.gui.setting.DoubleSetting;
import me.leeeaf.oakclient.gui.setting.IntegerSetting;
import me.leeeaf.oakclient.systems.commands.commands.SearchCommand;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.systems.renderer.Renderer;
import me.leeeaf.oakclient.systems.renderer.color.LineColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;

import java.awt.*;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class Tracers extends Module implements IEventListener {
    private final BooleanSetting shouldTraceSearchTargets = new BooleanSetting("Trace search entities", "traceSearchTargets", "Should trace search entities?", ()->true, false);
    private final IntegerSetting traceTargetsRange = new IntegerSetting("Search range", "searchRange", "Range for search targets", ()->true, 8, 512, 64);

    private final DoubleSetting widthSetting = new DoubleSetting("Width", "width", "Width of tracers", ()->true, 0.1,5,1.5);
    private final DoubleSetting opacitySetting = new DoubleSetting("Opacity", "opacity", "Opacity of tracers", ()->true, 0,1,0.75);

    private final BooleanSetting shouldTracePacificEntity = new BooleanSetting("Trace pacific entities", "tracePacificEntities", "Should trace pacific entities?", ()->true, false);
    private final ColorSetting pacificEntityColor = new ColorSetting("Pacific entities color", "pacificEntityColor", "Color of the pacific entities tracers", ()->true,false,false, Color.GREEN,false);

    private final BooleanSetting shouldTraceHostileEntity = new BooleanSetting("Trace hostile entities", "traceHostileEntities", "Should trace hostile entities?", ()->true, true);
    private final ColorSetting hostileEntityColor = new ColorSetting("Hostile entities color", "hostileEntityColor", "Color of the hostile entities tracers", ()->true,false,false, Color.RED,false);

    private final BooleanSetting shouldTraceItemEntity = new BooleanSetting("Trace item entities", "traceItemEntities", "Should trace item entities?", ()->true, true);
    private final ColorSetting itemEntityColor = new ColorSetting("Item entities color", "itemEntityColor", "Color of the item entities tracers", ()->true,false,false, Color.CYAN,false);




    public Tracers() {
        super("Tracers", "Renders lines to selected entities", ()->true, true, Category.RENDER);
        settings.add(traceTargetsRange);
        settings.add(shouldTraceSearchTargets);
        settings.add(widthSetting);
        settings.add(opacitySetting);
        settings.add(shouldTracePacificEntity);
        settings.add(pacificEntityColor);
        settings.add(shouldTraceHostileEntity);
        settings.add(hostileEntityColor);
        settings.add(shouldTraceItemEntity);
        settings.add(itemEntityColor);
    }

    @Override
    public void onDisable() {
        EventBus.getEventBus().unsubscribe(this);
    }

    @Override
    public void onEnable() {
        EventBus.getEventBus().subscribe(this);
    }

    @Override
    public void call(Object event) {
        float width = widthSetting.getValue().floatValue();
        int opacity = (int) (opacitySetting.getValue()*255);

        Vec3d vec2 = new Vec3d(0, 0, 75)
                .rotateX(-(float) Math.toRadians(mc.gameRenderer.getCamera().getPitch()))
                .rotateY(-(float) Math.toRadians(mc.gameRenderer.getCamera().getYaw()))
                .add(mc.cameraEntity.getEyePos());
        for (Entity e : mc.world.getEntities()) {
            Color col = getColor(e);

            if(col!=null){
                Vec3d vec = e.getPos().subtract(Renderer.getInterpolationOffset(e));
                LineColor lineColor =  LineColor.single(col.getRed(), col.getGreen(), col.getBlue(), opacity);
                Renderer.drawLine(vec2.x, vec2.y, vec2.z, vec.x, vec.y, vec.z, lineColor, width);
                Renderer.drawLine(vec.x, vec.y, vec.z, vec.x, vec.y + e.getHeight() * 0.9, vec.z, lineColor, width);
            }
        }
    }

//    private void traceSearchTargets(int range, Vec3d cameraVec3d) { //todo da rifare tutto in search
//        Integer renderDistance = mc.options.getViewDistance().getValue();
//        int distance = Math.min(range, renderDistance);
//        for (int chunkX = -distance; chunkX <= distance; chunkX++) {
//            for (int chunkZ = -distance; chunkZ <= distance; chunkZ++) {
//                WorldChunk chunk = mc.world.getChunkManager().getWorldChunk((int) mc.player.getX() / 16 + chunkX, (int) mc.player.getZ() / 16 + chunkZ);
//
//                if (chunk != null) {
//                        //search for blocks in chunk
//                    ChunkPos chunkPos = chunk.getPos();
//                    for(int x = 0; x<16; x++){
//                        for(int y = mc.world.getBottomY(); y<mc.world.getTopY(); y++){
//                            for(int z = 0; z<16; z++){
//                                BlockPos blockPos = new BlockPos(chunkPos.getStartX()+x,y,chunkPos.getStartZ()+z);
//                                BlockState blockState = chunk.getBlockState(blockPos);
//                                Block block = blockState.getBlock(); //TODO il problema è qui, il blocco trovato è sempre: translation{key='block.minecraft.void_air', args=[]}
//                                if(SearchCommand.searchTargets.contains(block)){
//                                    //render
//                                    LineColor lineColor =  LineColor.single(Color.PINK.getRed(), Color.PINK.getGreen(), Color.PINK.getBlue(), 0.75f);
//                                    Renderer.drawLine(cameraVec3d.x, cameraVec3d.y, cameraVec3d.z, blockPos.getX(), blockPos.getY(), blockPos.getZ(), lineColor, 1);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }


    private Color getColor(Entity e) {
        if(e == mc.player) return null;
        if(e instanceof PassiveEntity && shouldTracePacificEntity.getValue()) return pacificEntityColor.getColor();
        if(e instanceof Monster && shouldTraceHostileEntity.getValue()) return hostileEntityColor.getColor();
        if(e instanceof ItemEntity && shouldTraceItemEntity.getValue()) return itemEntityColor.getColor();
        return null;
    }

    @Override
    public Class<?>[] getTargets() {
        return new Class[]{WorldRenderEvent.Post.class};
    }
}
