package com.github.syr0ws.craftpack.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigLoader {

    private static final String CONFIG_FILE = "config.yml";

    public Config loadConfig() throws IOException {

        Path path = this.getConfigPath();

        if (Files.notExists(path)) {
            throw new FileNotFoundException("File '%s' does not exist.".formatted(path));
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        return mapper.readValue(path.toFile(), Config.class);
    }

    public void saveDefaultConfig() throws IOException {

        Path target = this.getConfigPath();

        if (Files.exists(target)) {
            return;
        }

        ClassLoader classLoader = this.getClass().getClassLoader();

        try (InputStream stream = classLoader.getResourceAsStream(CONFIG_FILE)) {

            if (stream == null) {
                throw new FileNotFoundException("Resource '%s' does not exist.".formatted(CONFIG_FILE));
            }

            Files.copy(stream, target);
        }
    }

    private Path getConfigPath() {
        return Paths.get("./" + CONFIG_FILE);
    }
}
