package me.leeeaf.oakclient.utils.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class FileHelper {
    private final Path workingDir;
    private static FileHelper instance;

    private FileHelper(){
        workingDir = Paths.get(mc.runDirectory.getPath(), "OakClient/");
        if (!workingDir.toFile().exists()) {
            workingDir.toFile().mkdirs();
        }
    }

    public static FileHelper getInstance(){
        if(instance == null){
            instance = new FileHelper();
        }
        return instance;
    }

    public String readFromFile(String fileName){
        Path filePath = workingDir.resolve(fileName);
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeToFile(String data, String fileName){
        Path filePath = workingDir.resolve(fileName);
        try {
            filePath.toFile().createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Files.write(filePath, data.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
