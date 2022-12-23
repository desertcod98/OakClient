package me.leeeaf.oakclient.mixin;

import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.events.io.KeyEvent;
import me.leeeaf.oakclient.utils.io.KeyAction;
import net.minecraft.client.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey", at=@At("HEAD"))
    void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci){
        if(key != GLFW.GLFW_KEY_UNKNOWN){
            EventBus.getEventBus().post(new KeyEvent(key, modifiers, KeyAction.getKeyAction(action)));
        }
    }
}
