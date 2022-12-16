package me.leeeaf.oakclient.systems.modules.movement;

import me.leeeaf.oakclient.event.EventSubscribe;
import me.leeeaf.oakclient.event.events.PacketEvent;
import me.leeeaf.oakclient.gui.setting.KeybindSetting;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.utils.FakePlayer;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.lwjgl.glfw.GLFW;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class Blink extends Module{
    public final KeybindSetting keybind=new KeybindSetting("Keybind","keybind","The key to toggle the module.",()->true, GLFW.GLFW_KEY_H);

    private FakePlayer fakePlayer;
    public Blink() {
        super("Blink", "Suspends sending of movement packets", ()->true, true, Category.MOVEMENT);
        settings.add(keybind);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        fakePlayer = new FakePlayer(mc.player);
        fakePlayer.spawn(); //todo first time enabling the module model does not spawn
    }

    @Override
    public void onDisable() {
        super.onDisable();
        fakePlayer.despawn();
    }

    @EventSubscribe
    public void onPacketSend(PacketEvent.Send event) {
        if(event.packet instanceof PlayerMoveC2SPacket){
            event.cancel();
        }
    }
}
