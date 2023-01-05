package me.leeeaf.oakclient;


import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.EventSubscribe;
import me.leeeaf.oakclient.event.events.PostTickEvent;
import me.leeeaf.oakclient.event.events.io.KeyEvent;
import me.leeeaf.oakclient.event.events.render.HudRenderEvent;
import me.leeeaf.oakclient.gui.ClickGUI;
import me.leeeaf.oakclient.gui.module.ClickGUIModule;
import me.leeeaf.oakclient.gui.module.HUDEditorModule;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.utils.io.KeyAction;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class OakClient implements ModInitializer {
    public static String splashText = "OakClient";


    //TODO create Friends system
    private static ClickGUI gui;
    private boolean inited=false;
    private final boolean keys[]=new boolean[266];
    @Override
    public void onInitialize() {
        Category.init();
        EventBus.getEventBus().subscribe(this);
    }

    @EventSubscribe
    public void onKey(KeyEvent event){
        if(mc.currentScreen == null){
            if (event.key==ClickGUIModule.keybind.getKey()){
                gui.enterGUI();
            }else if (event.key==HUDEditorModule.keybind.getKey()) gui.enterHUDEditor();
            else if(event.keyAction == KeyAction.PRESS){
                Category.toggleModuleByKeybind(event.key);
            }
        }
    }

    @EventSubscribe
    public void onPostTick(PostTickEvent event){
        if (!inited) {
            for (int i=32;i<keys.length;i++) keys[i]= GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(),i)==GLFW.GLFW_PRESS;
            gui=new ClickGUI();
            inited=true;
        }
//        if(mc.currentScreen == null){
//            for (int i=32;i<keys.length;i++) {
//                if (keys[i]!=(GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(),i)==GLFW.GLFW_PRESS)) {
//                    keys[i]=!keys[i];
//                    if (keys[i]) {
//
//                        ModulesWithKeybinds.toggleIfKeybind(i);
//                        if (i==ClickGUIModule.keybind.getKey()) gui.enterGUI();
//                        if (i==HUDEditorModule.keybind.getKey()) gui.enterHUDEditor();
//                        gui.handleKeyEvent(i);
//                    }
//                }
//            }
//        }
    }

    @EventSubscribe
    public void onHudRender(HudRenderEvent event){
        if(!inited && gui!=null){
            gui.render();
        }
    }
}
