package me.leeeaf.oakclient.systems.modules;

import com.lukflug.panelstudio.setting.ICategory;
import com.lukflug.panelstudio.setting.IClient;
import com.lukflug.panelstudio.setting.IModule;
import me.leeeaf.oakclient.gui.module.*;
import me.leeeaf.oakclient.gui.setting.KeybindSetting;
import me.leeeaf.oakclient.systems.modules.combat.*;
import me.leeeaf.oakclient.systems.modules.exploit.XCarry;
import me.leeeaf.oakclient.systems.modules.movement.*;
import me.leeeaf.oakclient.systems.modules.player.*;
import me.leeeaf.oakclient.systems.modules.render.*;
import me.leeeaf.oakclient.systems.modules.world.AntiCactus;
import me.leeeaf.oakclient.systems.modules.world.EntityOwner;
import me.leeeaf.oakclient.systems.modules.world.Portals;
import me.leeeaf.oakclient.systems.modules.world.XRay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public enum Category implements ICategory {
	COMBAT("Combat"),HUD("HUD"),MOVEMENT("Movement"),OTHER("Other"),RENDER("Render"),WORLD("World"),PLAYER("Player"), EXPLOIT("Exploit");
	public final String displayName;
	public final List<Module> modules= new ArrayList<>();
	
	Category (String displayName) {
		this.displayName=displayName;
	}

	public static void init(){
		addModule(new XRay());
		addModule(new TabGUIModule());
		addModule(new ClickGUIModule());
		addModule(new HUDEditorModule());
		addModule(new LogoModule());
		addModule(new WatermarkModule());
		addModule(new AntiHunger());
		addModule(new Fly());
		addModule(new ChestStealer());
		addModule(new NoFall());
		addModule(new Criticals());
		addModule(new Killaura());
		addModule(new XCarry());
		addModule(new AntiCactus());
		addModule(new Fullbright());
		addModule(new AntiKnockback());
		addModule(new AntiWeb());
		addModule(new HealthTags());
		addModule(new Portals());
		addModule(new Tracers());
		addModule(new AntiWeather());
		addModule(new Search());
		addModule(new AutoTotem());
		addModule(new AutoWeb());
		addModule(new Scaffold());
		addModule(new SafeWalk());
		addModule(new Blink());
		addModule(new Freecam());
		addModule(new ESP());
		addModule(new IgnoreServerPosition());
		addModule(new EntityOwner());
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public Stream<IModule> getModules() {
		return modules.stream().map(module->module);
	}

	public static void addModule(Module module){
		module.category.modules.add(module);
	}

	public static IClient getClient() {
		return () -> Arrays.stream(Category.values());
	}

	public static Module getModule(Class<? extends Module> moduleClass){
		for(Category category : Category.values()){
			Module module = (Module) category.getModules().filter(iModule -> iModule.getClass().equals(moduleClass)).findFirst().orElse(null);
			if(module!=null) return module;
		}
		return null;
	}

	public static Module getModule(String moduleDisplayName){
		for(Category category : Category.values()){
			Module module = (Module) category.getModules().filter(iModule -> iModule.getDisplayName().equals(moduleDisplayName)).findFirst().orElse(null);
			if(module!=null) return module;
		}
		return null;
	}

	public static void toggleModuleByKeybind(int key){
		for(Category category : Category.values()){
			for(IModule module: category.getModules().toList()){
				KeybindSetting keybindSetting = (KeybindSetting) module.getSettings()
						.filter(iSetting -> iSetting instanceof KeybindSetting)
						.findFirst().orElse(null);
				if(keybindSetting!=null && keybindSetting.getKey() != -1 && keybindSetting.getKey() == key){
					if(module.isEnabled() != null){
						module.isEnabled().toggle();
					}
					return;
				}
			}
		}
	}
}
