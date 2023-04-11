package me.leeeaf.oakclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TEST {
    public static void main(String[] args) throws IOException {
        Path workingDir = Paths.get("C:\\Users\\Utente\\Desktop\\Utilities\\Dev\\Java\\Minecraft\\OakClient\\run", "OakClient/");
        Path filePath = workingDir.resolve("modules.json");
        JsonElement jsonElement = JsonParser.parseString(Files.readString(filePath));
//        System.out.println(jsonElement.getAsJsonObject().get("Killaura").getAsJsonObject().get(''));
    }
}

