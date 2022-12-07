package me.leeeaf.oakclient.systems.modules.movement;

import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.IEventListener;
import me.leeeaf.oakclient.event.events.packets.PacketSendEvent;
import me.leeeaf.oakclient.gui.setting.KeybindSetting;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.utils.FakePlayer;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.lwjgl.glfw.GLFW;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class Blink extends Module implements IEventListener {
    public final KeybindSetting keybind=new KeybindSetting("Keybind","keybind","The key to toggle the module.",()->true, GLFW.GLFW_KEY_H);

    private FakePlayer fakePlayer;
    public Blink() {
        super("Blink", "Suspends sending of movement packets", ()->true, true, Category.MOVEMENT);
        settings.add(keybind);
    }

    @Override
    public void onEnable() {
        EventBus.getEventBus().subscribe(this);
        fakePlayer = new FakePlayer(mc.player, mc.player.getEntityName());
        fakePlayer.spawn(); //todo first time enabling the module model does not spawn
    }

    @Override
    public void onDisable() {
        EventBus.getEventBus().unsubscribe(this);
        fakePlayer.despawn();
    }

    @Override
    public void call(Object event) {
        if(((PacketSendEvent) event).packet instanceof PlayerMoveC2SPacket){
            ((PacketSendEvent) event).cancel();
        }
    }

    @Override
    public Class<?>[] getTargets() {
        return new Class[]{PacketSendEvent.class};
    }
}