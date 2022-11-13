package me.leeeaf.oakclient.systems;

import me.leeeaf.oakclient.gui.setting.KeybindSetting;
import me.leeeaf.oakclient.systems.modules.Module;

import java.util.HashMap;
import java.util.Map;

public class ModulesWithKeybinds {
    private static Map<KeybindSetting, Module> modulesWithKeybinds = new HashMap<>();

    public static void tryAddModule(Module module){
        KeybindSetting keybind = (KeybindSetting) module.settings.stream()
                .filter(setting -> setting instanceof KeybindSetting)
                .findFirst().orElse(null);
        if(keybind!=null){
            modulesWithKeybinds.put(keybind,module);
        }
    }

    public static void toggleIfKeybind(int i){
        modulesWithKeybinds.forEach((keybindSetting, module) -> {
            if(keybindSetting.getValue() == i && module.isEnabled()!=null){
                module.isEnabled().toggle();
            }
        });
    }
}
