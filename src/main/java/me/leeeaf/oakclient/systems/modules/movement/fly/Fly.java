package me.leeeaf.oakclient.systems.modules.movement.fly;

import me.leeeaf.oakclient.OakClientClient;
import me.leeeaf.oakclient.gui.setting.IntegerSetting;
import me.leeeaf.oakclient.gui.setting.KeybindSetting;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.systems.modules.Category;
import net.minecraft.entity.EntityPose;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class Fly extends Module {
    private int tickCounter;
    public final KeybindSetting keybind=new KeybindSetting("Keybind","keybind","The key to toggle the module.",()->true, GLFW.GLFW_KEY_R);
    public final IntegerSetting flySpeed = new IntegerSetting("Fly speed", "FlySpeed", "The speed at which you fly", ()->true,1,100,20);
    public Fly() {
        super("Fly", "Allows the player to fly", ()->true, true, Category.MOVEMENT);
        settings.add(keybind);
        settings.add(flySpeed);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        tickCounter = 0;
    }



    @Override
    public void onTick() {
        mc.player.getAbilities().setFlySpeed(flySpeed.getValue()*0.0015f);
        mc.player.getAbilities().flying = true;
        mc.player.setPose(EntityPose.STANDING);
        tickCounter++;
        Vec3d velocity = mc.player.getVelocity();
        if(tickCounter == 60){
            mc.player.setVelocity(new Vec3d(velocity.x, -0.4, velocity.z));
            tickCounter = 0;
        }

    }


}
