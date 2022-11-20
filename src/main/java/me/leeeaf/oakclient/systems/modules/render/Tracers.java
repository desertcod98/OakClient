package me.leeeaf.oakclient.systems.modules.render;

import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.IEventListener;
import me.leeeaf.oakclient.event.WorldRenderEvent;
import me.leeeaf.oakclient.gui.setting.ColorSetting;
import me.leeeaf.oakclient.gui.setting.DoubleSetting;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.systems.renderer.Renderer;
import me.leeeaf.oakclient.systems.renderer.color.LineColor;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class Tracers extends Module implements IEventListener {
    private final ColorSetting pacificEntityColor = new ColorSetting("Pacific entities color", "pacificEntityColor", "Color of the pacific entities tracers", ()->true,false,false, Color.RED,false);
    private final DoubleSetting widthSetting = new DoubleSetting("Width", "width", "Width of tracers", ()->true, 0.1,5,1.5);
    private final DoubleSetting opacitySetting = new DoubleSetting("Opacity", "opacity", "Opacity of tracers", ()->true, 0,1,0.75);
    public Tracers() {
        super("tracer", "tracer", ()->true, true, Category.RENDER);
        settings.add(pacificEntityColor);
        settings.add(widthSetting);
        settings.add(opacitySetting);
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
    public void onTick() {

    }

    @Override
    public void call(Object event) {
        float width = widthSetting.getValue().floatValue();
        int opacity = (int) (opacitySetting.getValue()*255);

        for (Entity e : mc.world.getEntities()) {
            int[] col = {pacificEntityColor.getColor().getRed(),pacificEntityColor.getColor().getGreen(),pacificEntityColor.getColor().getBlue()};

            if (col != null) {
                Vec3d vec = e.getPos().subtract(Renderer.getInterpolationOffset(e));
                Vec3d vec2 = new Vec3d(0, 0, 75)
                        .rotateX(-(float) Math.toRadians(mc.gameRenderer.getCamera().getPitch()))
                        .rotateY(-(float) Math.toRadians(mc.gameRenderer.getCamera().getYaw()))
                        .add(mc.cameraEntity.getEyePos());

                LineColor lineColor =  LineColor.single(col[0], col[1], col[2], opacity);
                Renderer.drawLine(vec2.x, vec2.y, vec2.z, vec.x, vec.y, vec.z, lineColor, width);
                Renderer.drawLine(vec.x, vec.y, vec.z, vec.x, vec.y + e.getHeight() * 0.9, vec.z, lineColor, width);
            }
        }
    }

    @Override
    public Class<?> getTarget() {
        return WorldRenderEvent.Post.class;
    }
}
