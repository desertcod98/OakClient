package me.leeeaf.oakclient;

import me.leeeaf.oakclient.gui.ClickGUI;
import me.leeeaf.oakclient.gui.module.*;
import me.leeeaf.oakclient.gui.setting.KeybindSetting;
import me.leeeaf.oakclient.systems.ModulesWithKeybinds;
import me.leeeaf.oakclient.systems.modules.Category;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class OakClient implements ModInitializer {
    private static ClickGUI gui;
    private boolean inited=false;
    private final boolean keys[]=new boolean[266];
    @Override
    public void onInitialize() {
        Category.init();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!inited) {
                for (int i=32;i<keys.length;i++) keys[i]= GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(),i)==GLFW.GLFW_PRESS;
                gui=new ClickGUI();
                HudRenderCallback.EVENT.register((cli, tickDelta)->gui.render());
                inited=true;
            }
            for (int i=32;i<keys.length;i++) {
                if (keys[i]!=(GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(),i)==GLFW.GLFW_PRESS)) {
                    keys[i]=!keys[i];
                    if (keys[i]) {

                        ModulesWithKeybinds.toggleIfKeybind(i);
                        if (i==ClickGUIModule.keybind.getKey()) gui.enterGUI();
                        if (i==HUDEditorModule.keybind.getKey()) gui.enterHUDEditor();
                        gui.handleKeyEvent(i);
                    }
                }
            }
        });
    }
}
