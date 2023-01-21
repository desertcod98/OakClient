package me.leeeaf.oakclient.systems.modules;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.setting.IModule;
import com.lukflug.panelstudio.setting.ISetting;
import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.gui.setting.KeybindSetting;
import me.leeeaf.oakclient.gui.setting.Setting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public abstract class Module implements IModule {
	public final String displayName,description;
	public final IBoolean visible;
	public final Category category;
	public final List<Setting<?>> settings= new ArrayList<>();
	public final boolean toggleable;
	private boolean enabled=false;
	private boolean subscribed = false;

	public Module (String displayName, String description, IBoolean visible, boolean toggleable, Category category) {
		this.displayName=displayName;
		this.description=description;
		this.visible=visible;
		this.toggleable=toggleable;
		this.category = category;

		KeybindSetting keybindSetting = new KeybindSetting("Keybind", "keybind", "The key to toggle the module", () -> true, null);
		settings.add(keybindSetting);
	}
	
	@Override
	public String getDisplayName() {
		return displayName;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public IBoolean isVisible() {
		return visible;
	}

	@Override
	public IToggleable isEnabled() {
		if (!toggleable) return null;
		return new IToggleable() {
			@Override
			public boolean isOn() {
				return enabled;
			}

			@Override
			public void toggle() {
				enabled=!enabled;
				if(enabled){
					onEnable();
				}else{
					onDisable();
				}
			}
		};
	}

	public void onDisable(){
		if(subscribed){
			EventBus.getEventBus().unsubscribe(this);
		}
	}

	public void onEnable(){
		if(EventBus.getEventBus().subscribe(this)){
			subscribed = true;
		}
	}

	public void onTick(){}

	public List<Setting<?>> getSettingsInstances(){
		return settings;
	}

	@Override
	public Stream<ISetting<?>> getSettings() {
		return settings.stream().filter(setting->setting instanceof ISetting).sorted(Comparator.comparing(a -> a.displayName)).map(setting->(ISetting<?>)setting);
	}
}
