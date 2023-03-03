package me.leeeaf.oakclient;


import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.EventSubscribe;
import me.leeeaf.oakclient.event.events.PostTickEvent;
import me.leeeaf.oakclient.event.events.io.KeyEvent;
import me.leeeaf.oakclient.event.events.render.HudRenderEvent;
import me.leeeaf.oakclient.gui.ClickGUI;
import me.leeeaf.oakclient.gui.module.ClickGUIModule;
import me.leeeaf.oakclient.gui.module.HUDEditorModule;
import me.leeeaf.oakclient.systems.SaveHelper;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.utils.file.FileHelper;
import me.leeeaf.oakclient.utils.io.KeyAction;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class OakClient implements ModInitializer, ClientModInitializer {
    public static String splashText = "OakClient";
    public static MinecraftClient mc;

    private static ClickGUI gui;
    private boolean inited=false;
    @Override
    public void onInitialize() {
        Category.init();
        EventBus.getEventBus().subscribe(this);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            SaveHelper.getInstance().saveAllSystems();
        }));
    }

    @EventSubscribe
    public void onKey(KeyEvent event){
        FileHelper.getInstance();
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
            gui=new ClickGUI();
            inited=true;
        }
    }

    @EventSubscribe
    public void onHudRender(HudRenderEvent event){
        if(!inited && gui!=null){
            gui.render();
        }
    }

    @Override
    public void onInitializeClient() {
        mc = MinecraftClient.getInstance();
        SaveHelper.getInstance().loadAllSystems();
    }
}
