package com.sbg.trafiklab.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

    public static void createFileIfNotPresent(Path path) throws IOException {
        if (Files.notExists(path)) {
            Files.createFile(path);
        }
    }

}
