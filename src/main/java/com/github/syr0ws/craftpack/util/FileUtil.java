package com.github.syr0ws.craftpack.util;

import com.github.syr0ws.craftpack.Main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

    public static void copyResource(String resourcePath, Path output) throws IOException {

        ClassLoader classLoader = Main.class.getClassLoader();

        try (InputStream stream = classLoader.getResourceAsStream(resourcePath)) {

            if (stream == null) {
                throw new FileNotFoundException("Resource '%s' does not exist.".formatted(resourcePath));
            }

            Files.createDirectories(output.getParent());
            Files.copy(stream, output);
        }
    }
}
