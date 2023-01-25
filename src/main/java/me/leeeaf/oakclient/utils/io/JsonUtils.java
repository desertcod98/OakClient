package me.leeeaf.oakclient.utils.io;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonUtils {
    private static final Gson gson = new Gson();

    public static JsonObject mergeJsonObjects(JsonObject o1, JsonObject o2){
        if(o1.size()<=0){
            return o2;
        }else if(o2.size()<=0){
            return o1;
        }

        String o1str = o1.toString();
        String o2str = o2.toString();

        String result = o1str.substring(0, o1str.length()-1) + "," + o2str.substring(1);
        return gson.fromJson(result, JsonObject.class);
    }
}
