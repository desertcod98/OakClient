package me.leeeaf.oakclient.systems.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import com.lukflug.panelstudio.setting.ICategory;
import com.lukflug.panelstudio.setting.IClient;
import com.lukflug.panelstudio.setting.IModule;
import me.leeeaf.oakclient.gui.module.*;
import me.leeeaf.oakclient.systems.ModulesWithKeybinds;
import me.leeeaf.oakclient.systems.modules.combat.*;
import me.leeeaf.oakclient.systems.modules.exploit.SecretClose;
import me.leeeaf.oakclient.systems.modules.movement.Blink;
import me.leeeaf.oakclient.systems.modules.movement.Fly;
import me.leeeaf.oakclient.systems.modules.movement.SafeWalk;
import me.leeeaf.oakclient.systems.modules.movement.Scaffold;
import me.leeeaf.oakclient.systems.modules.player.*;
import me.leeeaf.oakclient.systems.modules.render.HealthTags;
import me.leeeaf.oakclient.systems.modules.render.Search;
import me.leeeaf.oakclient.systems.modules.render.Tracers;
import me.leeeaf.oakclient.systems.modules.world.*;

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
		addModule(new Clicker());
		addModule(new AntiHunger());
		addModule(new Fly());
		addModule(new ChestStealer());
		addModule(new NoFall());
		addModule(new Criticals());
		addModule(new Killaura());
		addModule(new SecretClose());
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
		ModulesWithKeybinds.tryAddModule(module);
	}

	public static IClient getClient() {
		return () -> Arrays.stream(Category.values());
	}
}
