package me.leeeaf.oakclient.utils.io;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public class JsonUtils {

    public static void mergeJsonObject(JsonObject obj1, JsonObject obj2){
        for (Map.Entry<String, JsonElement> entry : obj2.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (obj1.has(key)) {
                JsonElement existingValue = obj1.get(key);
                if (existingValue.isJsonObject() && value.isJsonObject()) {
                    mergeJsonObject(existingValue.getAsJsonObject(), value.getAsJsonObject());
                } else {
                    obj1.add(key, value);
                }
            } else {
                obj1.add(key, value);
            }
        }
    }
}
