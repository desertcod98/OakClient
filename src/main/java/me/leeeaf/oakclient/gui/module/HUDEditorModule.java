package me.leeeaf.oakclient.gui.module;

import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import org.lwjgl.glfw.GLFW;

import me.leeeaf.oakclient.gui.setting.BooleanSetting;
import me.leeeaf.oakclient.gui.setting.KeybindSetting;

public class HUDEditorModule extends Module {
	public static final BooleanSetting showHUD=new BooleanSetting("Show HUD Panels","showHUD","Whether to show the HUD panels in the ClickGUI.",()->true,true);
	public static final KeybindSetting keybind=new KeybindSetting("Keybind","keybind","The key to toggle the module.",()->true,GLFW.GLFW_KEY_EQUAL);
	
	public HUDEditorModule() {
		super("HUDEditor","Module containing HUDEditor settings.",()->true,false, Category.OTHER);
		settings.add(showHUD);
		settings.add(keybind);
	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onTick() {

	}
}
