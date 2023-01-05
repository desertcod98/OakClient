package me.leeeaf.oakclient.systems.modules.render;

import me.leeeaf.oakclient.event.EventSubscribe;
import me.leeeaf.oakclient.event.events.render.WorldRenderEvent;
import me.leeeaf.oakclient.gui.setting.BooleanSetting;
import me.leeeaf.oakclient.gui.setting.ColorSetting;
import me.leeeaf.oakclient.gui.setting.DoubleSetting;
import me.leeeaf.oakclient.gui.setting.IntegerSetting;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.systems.renderer.Renderer;
import me.leeeaf.oakclient.systems.renderer.color.LineColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class Tracers extends Module {
    private final DoubleSetting widthSetting = new DoubleSetting("Width", "width", "Width of tracers", ()->true, 0.1,5,1.5);
    private final DoubleSetting opacitySetting = new DoubleSetting("Opacity", "opacity", "Opacity of tracers", ()->true, 0,1,0.75);

    private final BooleanSetting shouldTracePlayers = new BooleanSetting("Trace players", "tracePlayers", "Should trace players?", ()->true, true);
    private final ColorSetting playersColor = new ColorSetting("Players color", "playersColor", "Color of players tracers", ()->true,false,false, Color.ORANGE,false);


    private final BooleanSetting shouldTracePacificEntity = new BooleanSetting("Trace pacific entities", "tracePacificEntities", "Should trace pacific entities?", ()->true, false);
    private final ColorSetting pacificEntityColor = new ColorSetting("Pacific entities color", "pacificEntityColor", "Color of the pacific entities tracers", ()->true,false,false, Color.GREEN,false);

    private final BooleanSetting shouldTraceHostileEntity = new BooleanSetting("Trace hostile entities", "traceHostileEntities", "Should trace hostile entities?", ()->true, true);
    private final ColorSetting hostileEntityColor = new ColorSetting("Hostile entities color", "hostileEntityColor", "Color of the hostile entities tracers", ()->true,false,false, Color.RED,false);

    private final BooleanSetting shouldTraceItemEntity = new BooleanSetting("Trace item entities", "traceItemEntities", "Should trace item entities?", ()->true, true);
    private final ColorSetting itemEntityColor = new ColorSetting("Item entities color", "itemEntityColor", "Color of the item entities tracers", ()->true,false,false, Color.CYAN,false);

    private final IntegerSetting range = new IntegerSetting("Range", "range", "Range to search entities in", ()->true, 1, 32, 8);

    //TODO add range setting

    public Tracers() {
        super("Tracers", "Renders lines to selected entities", ()->true, true, Category.RENDER);
        settings.add(widthSetting);
        settings.add(opacitySetting);
        settings.add(shouldTracePlayers);
        shouldTracePlayers.subSettings.add(playersColor);
        settings.add(shouldTracePacificEntity);
        shouldTracePacificEntity.subSettings.add(pacificEntityColor);
        settings.add(shouldTraceHostileEntity);
        shouldTraceHostileEntity.subSettings.add(hostileEntityColor);
        settings.add(shouldTraceItemEntity);
        shouldTraceItemEntity.subSettings.add(itemEntityColor);
        settings.add(range);
    }

    @EventSubscribe
    public void onWorldRenderPost(WorldRenderEvent.Post event) {
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

    private Color getColor(Entity e) {
        if(mc.player.squaredDistanceTo(e) > (range.getValue() * 16)*(range.getValue() * 16)){
            return null;
        }
        if(e == mc.player) return null;
        if(e instanceof PassiveEntity && shouldTracePacificEntity.getValue()) return pacificEntityColor.getColor();
        if(e instanceof Monster && shouldTraceHostileEntity.getValue()) return hostileEntityColor.getColor();
        if(e instanceof ItemEntity && shouldTraceItemEntity.getValue()) return itemEntityColor.getColor();
        if(e instanceof PlayerEntity && e!= mc.player && shouldTracePlayers.getValue()) return playersColor.getColor();
        return null;
    }
}
