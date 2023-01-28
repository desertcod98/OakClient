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
import me.leeeaf.oakclient.utils.io.JsonUtils;

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

    private void saveModules() {
        JsonObject json = new JsonObject();
        for (Category category : Category.values()) {

            category.getModules().forEach(module -> {

                JsonObject moduleJson = new JsonObject();
                JsonObject moduleSettingsJson = new JsonObject();
                for (Setting<?> setting : ((Module) module).getSettingsInstances()) {

                    JsonObject tempModuleSettingsJson = getSettingJson(setting);
                    JsonUtils.mergeJsonObject(moduleSettingsJson, tempModuleSettingsJson);
                }
                if (moduleSettingsJson.size() > 0) { //don't create "settings" key if module has no settings set
                    moduleJson.add("settings", moduleSettingsJson);
                }
                moduleJson.add("enabled", new JsonPrimitive(module.isEnabled() != null && module.isEnabled().isOn()));
                json.add(module.getDisplayName(), moduleJson);
            });
        }
        try {
            FileHelper.getInstance().writeToFile(json.toString(), "modules.json");
        } catch (IOException e) {
            ChatLogger.error("Couldn't write to file modules.json!");
        }
    }

    private JsonObject getSettingJson(Setting<?> setting) {
        JsonObject settingJson = new JsonObject();

        if (setting.getSubSettingsInstances().size() != 0) {
            JsonObject subSettingJson = new JsonObject();
            for (Setting<?> subSetting : setting.getSubSettingsInstances()) {
                JsonUtils.mergeJsonObject(subSettingJson, getSettingJson(subSetting));
            }
            settingJson.add("subSettings", subSettingJson);
        }

        try {
            if (setting instanceof BooleanSetting) {
                settingJson.add(setting.getConfigName(), new JsonPrimitive(((BooleanSetting) setting).getValue()));
            } else if (setting instanceof IntegerSetting) {
                settingJson.add(setting.getConfigName(), new JsonPrimitive(((IntegerSetting) setting).getValue()));
            } else if (setting instanceof DoubleSetting) {
                settingJson.add(setting.getConfigName(), new JsonPrimitive(((DoubleSetting) setting).getValue()));
            } else if (setting instanceof ColorSetting) {
                settingJson.add(setting.getConfigName(), new JsonPrimitive(((ColorSetting) setting).getValue().getRGB()));
            } else if (setting instanceof EnumSetting<?>) {
                settingJson.add(setting.getConfigName(), new JsonPrimitive(((EnumSetting<?>) setting).getValueIndex()));
            } else if (setting instanceof KeybindSetting) {
                settingJson.add(setting.getConfigName(), new JsonPrimitive(((KeybindSetting) setting).getValue()));
            } else if (setting instanceof StringSetting) {
                settingJson.add(setting.getConfigName(), new JsonPrimitive(((StringSetting) setting).getValue()));
            }
        } catch (NullPointerException ignored) {
        }
        return settingJson;
    }

    private void loadModules() {
        String modulesJsonString = null;
        try {
            modulesJsonString = FileHelper.getInstance().readFromFile("modules.json");
        } catch (IOException e) {
            ChatLogger.error("Couldn't read file modules.json!");
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

            if (value.has("settings")) {
                JsonObject settings = value.getAsJsonObject("settings");

                JsonObject subSettings = null;

                if (settings.has("subSettings")) {
                    subSettings = settings.getAsJsonObject("subSettings");
                }

                for (String settingKey : settings.keySet()) {
                    Setting<?> setting = module.getSetting(settingKey);
                    if (setting != null) {
                        setSettingValue(setting, settings.get(settingKey));
                        if (subSettings != null) {
                            for (String subSettingKey : subSettings.keySet()) {
                                Setting<?> subSetting = setting.getSubSettingsInstances().stream()
                                        .filter(subSettingInstance -> subSettingInstance.getConfigName().equals(subSettingKey))
                                        .findFirst()
                                        .orElse(null);
                                if (subSetting != null) {
                                    setSettingValue(subSetting, subSettings.get(subSettingKey));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void setSettingValue(Setting<?> setting, JsonElement value) {
        if (setting instanceof BooleanSetting) {
            ((BooleanSetting) setting).setValue(value.getAsBoolean());
        } else if (setting instanceof IntegerSetting) {
            ((IntegerSetting) setting).setValue(value.getAsInt());
        } else if (setting instanceof DoubleSetting) {
            ((DoubleSetting) setting).setValue(value.getAsDouble());
        } else if (setting instanceof ColorSetting) {
            ((ColorSetting) setting).setValue(new Color(value.getAsInt()));
        } else if (setting instanceof EnumSetting<?>) {
            ((EnumSetting<?>) setting).setValueIndex(value.getAsInt());
        } else if (setting instanceof KeybindSetting) {
            ((KeybindSetting) setting).setValue(value.getAsInt());
        } else if (setting instanceof StringSetting) {
            ((StringSetting) setting).setValue(value.getAsString());
        }
    }
}
