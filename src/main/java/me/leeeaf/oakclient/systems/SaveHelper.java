package me.leeeaf.oakclient.systems;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import me.leeeaf.oakclient.gui.setting.*;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.utils.file.FileHelper;
import me.leeeaf.oakclient.utils.io.ChatLogger;

import java.awt.*;
import java.io.IOException;

public class SaveHelper {
    private static SaveHelper instance;

    private SaveHelper() {
    }

    public static SaveHelper getInstance() {
        if (instance == null) {
            instance = new SaveHelper();
        }
        return instance;
    }

    public void saveAllSystems() {
        saveModules();
    }

    public void loadAllSystems() {
        loadModules();
    }

    private void saveModules() { //TODO manage subsettings
        JsonObject json = new JsonObject();
        for (Category category : Category.values()) {
            category.getModules().forEach(iModule -> {
                JsonObject moduleJson = new JsonObject();
                JsonObject moduleSettingsJson = new JsonObject();
                ((Module) iModule).getSettingsInstances().forEach(setting -> {
                    try {
                        if (setting instanceof BooleanSetting) {
                            moduleSettingsJson.add(setting.getConfigName(), new JsonPrimitive(((BooleanSetting) setting).getValue()));
                        } else if (setting instanceof IntegerSetting) {
                            moduleSettingsJson.add(setting.getConfigName(), new JsonPrimitive(((IntegerSetting) setting).getValue()));
                        } else if (setting instanceof DoubleSetting) {
                            moduleSettingsJson.add(setting.getConfigName(), new JsonPrimitive(((DoubleSetting) setting).getValue()));
                        } else if (setting instanceof ColorSetting) {
                            moduleSettingsJson.add(setting.getConfigName(), new JsonPrimitive(((ColorSetting) setting).getValue().getRGB()));
                        } else if (setting instanceof EnumSetting<?>) {
                            moduleSettingsJson.add(setting.getConfigName(), new JsonPrimitive(((EnumSetting<?>) setting).getValueIndex()));
                        } else if (setting instanceof KeybindSetting) {
                            moduleSettingsJson.add(setting.getConfigName(), new JsonPrimitive(((KeybindSetting) setting).getValue()));
                        } else if (setting instanceof StringSetting) {
                            moduleSettingsJson.add(setting.getConfigName(), new JsonPrimitive(((StringSetting) setting).getValue()));
                        }
                    } catch (NullPointerException ignored) {
                    }
                });
                if (moduleSettingsJson.size() != 0) { //don't create "settings" key if module has no settings set
                    moduleJson.add("settings", moduleSettingsJson);
                }
                moduleJson.add("enabled", new JsonPrimitive(iModule.isEnabled() != null && iModule.isEnabled().isOn()));
                json.add(iModule.getDisplayName(), moduleJson);
            });
        }
        try {
            FileHelper.getInstance().writeToFile(json.toString(), "modules.json");
        } catch (IOException e) {
            ChatLogger.error("Couldn't save modules to file modules.json!");
        }
    }

    private void loadModules() {
        String modulesJsonString = null;
        try {
            modulesJsonString = FileHelper.getInstance().readFromFile("modules.json");
        } catch (IOException e) {
            ChatLogger.error("Couldn't loAd modules from file modules.json!");
            return;
        }
        JsonObject modulesJson = JsonParser.parseString(modulesJsonString).getAsJsonObject();

        for (String key : modulesJson.keySet()) {
            Module module = Category.getModule(key);
            if (module == null) {
                continue;
            }

            JsonObject value = modulesJson.getAsJsonObject(key);

            if (value.get("enabled").getAsBoolean() && module.isEnabled() != null) {
                module.isEnabled().toggle();
            }

            if(value.has("settings")){
                JsonObject settings = value.getAsJsonObject("settings");
                for(String settingKey : settings.keySet()){
                    Setting<?> setting = module.getSetting(settingKey);
                    if(setting!=null){
                        if (setting instanceof BooleanSetting) {
                            ((BooleanSetting)setting).setValue(settings.get(settingKey).getAsBoolean());
                        } else if (setting instanceof IntegerSetting) {
                            ((IntegerSetting)setting).setValue(settings.get(settingKey).getAsInt());
                        } else if (setting instanceof DoubleSetting) {
                            ((DoubleSetting)setting).setValue(settings.get(settingKey).getAsDouble());
                        } else if (setting instanceof ColorSetting) {
                            ((ColorSetting)setting).setValue(new Color(settings.get(settingKey).getAsInt()));
                        } else if (setting instanceof EnumSetting<?>) {
                            ((EnumSetting<?>)setting).setValueIndex(settings.get(settingKey).getAsInt());
                        } else if (setting instanceof KeybindSetting) {
                            ((KeybindSetting)setting).setValue(settings.get(settingKey).getAsInt());
                        } else if (setting instanceof StringSetting) {
                            ((StringSetting)setting).setValue(settings.get(settingKey).getAsString());
                        }
                    }
                }
            }
        }
    }
}
