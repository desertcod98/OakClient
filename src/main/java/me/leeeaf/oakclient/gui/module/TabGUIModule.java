package me.leeeaf.oakclient.gui.module;

import com.lukflug.panelstudio.base.Animation;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.component.IFixedComponent;
import com.lukflug.panelstudio.container.IContainer;
import com.lukflug.panelstudio.setting.IClient;
import com.lukflug.panelstudio.tabgui.ITabGUITheme;
import com.lukflug.panelstudio.tabgui.StandardTheme;
import com.lukflug.panelstudio.tabgui.TabGUI;
import com.lukflug.panelstudio.theme.IColorScheme;
import com.lukflug.panelstudio.theme.ITheme;
import me.leeeaf.oakclient.gui.setting.ColorSetting;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.function.Supplier;

public class TabGUIModule extends Module {
	private static TabGUIModule instance;
	private static ITabGUITheme theme;
	
	public TabGUIModule() {
		super("TabGUI","HUD module that lets toggle modules.",()->true,true, Category.OTHER);
		instance=this;
		theme=new StandardTheme(new IColorScheme() {
			@Override
			public void createSetting(ITheme theme, String name, String description, boolean hasAlpha, boolean allowsRainbow, Color color, boolean rainbow) {
				ColorSetting setting=new ColorSetting(name,name,description,()->true,allowsRainbow,hasAlpha,color,rainbow);
				instance.settings.add(setting);
			}

			@Override
			public Color getColor (String name) {
				return (Color)instance.settings.stream().filter(setting->setting.configName.equals(name)).findFirst().orElse(null).getValue();
			}
		},75,9,2,5);
	}

	public static IFixedComponent getComponent (IClient client, IContainer<IFixedComponent> container, Supplier<Animation> animation) {
		return new TabGUI(()->"TabGUI",client,theme,container,animation,key->key==GLFW.GLFW_KEY_UP,key->key==GLFW.GLFW_KEY_DOWN,key->key==GLFW.GLFW_KEY_ENTER||key==GLFW.GLFW_KEY_RIGHT,key->key==GLFW.GLFW_KEY_LEFT,new Point(10,10),"tabGUI").getWrappedComponent();
	}
	
	public static IToggleable getToggle() {
		return instance.isEnabled();
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
