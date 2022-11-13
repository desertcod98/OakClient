package me.leeeaf.oakclient.systems.modules.movement.fly;

import me.leeeaf.oakclient.OakClientClient;
import me.leeeaf.oakclient.gui.setting.KeybindSetting;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.systems.modules.Category;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Fly extends Module {
    private int tickCounter;
    public static final KeybindSetting keybind=new KeybindSetting("Keybind","keybind","The key to toggle the module.",()->true, GLFW.GLFW_KEY_R); //todo doesent work

    public Fly() {
        super("Fly", "Allows the player to fly", ()->true, true, Category.MOVEMENT);
        settings.add(keybind);

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
        tickCounter++;
        Vec3d velocity = OakClientClient.mc.player.getVelocity();
        double motionY = 0;
        if(OakClientClient.mc.options.jumpKey.isPressed()) motionY += 0.5;
        if(OakClientClient.mc.options.sneakKey.isPressed()) motionY -= 0.5;
        if(tickCounter == 60){
            motionY = -0.4;
            tickCounter = 0;
        }
        OakClientClient.mc.player.setVelocity(new Vec3d(velocity.x, motionY, velocity.z));
    }


}
