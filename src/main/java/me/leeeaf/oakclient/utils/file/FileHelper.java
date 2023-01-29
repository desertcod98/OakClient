package me.leeeaf.oakclient.utils.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static me.leeeaf.oakclient.OakClient.mc;

public class FileHelper {
    private final Path workingDir;
    private static FileHelper instance;

    private FileHelper() {
        workingDir = Paths.get(mc.runDirectory.getPath(), "OakClient/");
        if (!workingDir.toFile().exists()) {
            workingDir.toFile().mkdirs();
        }
    }

    public static FileHelper getInstance() {
        if (instance == null) {
            instance = new FileHelper();
        }
        return instance;
    }

    public String readFromFile(String fileName) throws IOException {
        Path filePath = workingDir.resolve(fileName);
        return Files.readString(filePath);
    }

    public void writeToFile(String data, String fileName) throws IOException {
        Path filePath = workingDir.resolve(fileName);
        filePath.toFile().createNewFile();
        Files.write(filePath, data.getBytes());

    }
}
