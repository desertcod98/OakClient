package me.leeeaf.oakclient.systems;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import me.leeeaf.oakclient.gui.setting.*;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.utils.file.FileHelper;

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

    private void loadAllSystems(){
        loadModules();
    }

    private void saveModules() {
        JsonObject json = new JsonObject();
        for (Category category : Category.values()) {
            category.getModules().forEach(iModule -> {
                JsonObject moduleJson = new JsonObject();
                JsonObject moduleSettingsJson = new JsonObject();
                ((Module) iModule).getSettingsInstances().forEach(setting -> {
                    try{
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
                    }catch (NullPointerException ignored){}
                });
                if(moduleSettingsJson.size()!=0){ //don't create "settings" key if module has no settings set
                    moduleJson.add("settings", moduleSettingsJson);
                }
                if(moduleJson.size()!=0){  //don't save module if module has no proprieties set
                    json.add(iModule.getDisplayName(), moduleJson);
                }
            });
        }
        FileHelper.getInstance().writeToFile(json.toString(), "modules.json");
    }

    private void loadModules(){
        String modulesJsonString = FileHelper.getInstance().readFromFile("modules.json");
        JsonObject modulesJson = JsonParser.parseString(modulesJsonString).getAsJsonObject();
        //TODO complete loading of modules
    }
}
