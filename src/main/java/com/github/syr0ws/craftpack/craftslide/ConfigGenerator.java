package com.github.syr0ws.craftpack.craftslide;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.syr0ws.craftpack.resourcepack.ResourcePackGenerationResult;
import com.github.syr0ws.craftpack.resourcepack.model.Background;
import com.github.syr0ws.craftpack.resourcepack.model.DynamicBackground;
import com.github.syr0ws.craftpack.resourcepack.model.FixedBackground;
import com.github.syr0ws.craftpack.resourcepack.model.Image;
import com.github.syr0ws.craftpack.util.Util;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ConfigGenerator {

    private static final String OUTPUT_FOLDER_NAME = "craftslide";
    private static final String IMAGES_FILE_NAME = "images.yml";
    private static final String BACKGROUNDS_FILE_NAME = "backgrounds.yml";

    public void generate(ResourcePackGenerationResult result) throws IOException {
        this.createOutputFolder();
        this.generateImagesConfig(result.images());
        this.generateBackgroundsConfig(result.backgrounds());
    }

    private void createOutputFolder() throws IOException {

        Path folder = this.getOutputFolder();
        FileUtils.deleteDirectory(folder.toFile());

        if (!Files.isDirectory(folder)) {
            Files.createDirectory(folder);
        }
    }

    private void generateImagesConfig(List<Image> images) throws IOException {

        List<ImageConfig> configs = images.stream()
                .map(ImageConfig::from)
                .toList();

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        String content = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ImagesConfig(configs));
        content = Util.removeCharsDoubleSlash(content);

        Path outputFile = this.getOutputFile(IMAGES_FILE_NAME);
        Files.writeString(outputFile, content);
    }

    private void generateBackgroundsConfig(List<Background> backgrounds) throws IOException {

        List<BackgroundConfig> configs = backgrounds.stream()
                .map(this::generateBackgroundsConfig)
                .toList();

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        String content = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new BackgroundsConfig(configs));
        content = Util.removeCharsDoubleSlash(content);

        Path outputFile = this.getOutputFile(BACKGROUNDS_FILE_NAME);
        Files.writeString(outputFile, content);
    }

    private BackgroundConfig generateBackgroundsConfig(Background background) {

        // Not the best option but as we only have two background types, it is the easiest.
        if(background instanceof FixedBackground config) {
            return FixedBackgroundConfig.from(config);
        } else if(background instanceof DynamicBackground config) {
            return DynamicBackgroundConfig.from(config);
        } else {
            throw new IllegalArgumentException("Background type not supported");
        }
    }

    private Path getOutputFile(String fileName) {
        return Paths.get(this.getOutputFolder() + File.separator + fileName);
    }

    private Path getOutputFolder() {
        return Paths.get("./" + OUTPUT_FOLDER_NAME);
    }
}
