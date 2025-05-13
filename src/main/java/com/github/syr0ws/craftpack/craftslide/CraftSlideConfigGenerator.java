package com.github.syr0ws.craftpack.craftslide;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.syr0ws.craftpack.resourcepack.ResourcePackGenerationResult;
import com.github.syr0ws.craftpack.resourcepack.model.Background;
import com.github.syr0ws.craftpack.resourcepack.model.Image;
import com.github.syr0ws.craftpack.util.Util;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CraftSlideConfigGenerator {

    private static final String OUTPUT_FOLDER_NAME = "craftslide";
    private static final String IMAGES_FILE_NAME = "images.yml";
    private static final String BACKGROUNDS_FILE_NAME = "backgrounds.yml";

    public void generate(ResourcePackGenerationResult result) throws IOException {
        this.createOutputFolder();
        this.generateCraftSlideImagesConfig(result.images());
        this.generateCraftSlideBackgroundsConfig(result.backgrounds());
    }

    private void createOutputFolder() throws IOException {

        Path folder = this.getOutputFolder();
        FileUtils.deleteDirectory(folder.toFile());

        if (!Files.isDirectory(folder)) {
            Files.createDirectory(folder);
        }
    }

    private void generateCraftSlideImagesConfig(List<Image> images) throws IOException {

        List<CraftSlideImageConfig> configs = images.stream()
                .map(image -> new CraftSlideImageConfig(
                        image.imageId(),
                        image.tileHeight(),
                        image.rows(),
                        image.columns(),
                        image.providers().stream().map(provider -> provider.chars().get(0)).toList()
                )).toList();

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        String content = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new CraftSlideImagesConfig(configs));
        content = Util.removeCharsDoubleSlash(content);

        Path outputFile = this.getOutputFile(IMAGES_FILE_NAME);
        Files.writeString(outputFile, content);
    }

    private void generateCraftSlideBackgroundsConfig(List<Background> backgrounds) throws IOException {

        List<CraftSlideBackgroundConfig> configs = backgrounds.stream()
                .map(background -> new CraftSlideBackgroundConfig(
                        background.backgroundId(),
                        background.height(),
                        background.providers().stream().map(provider -> provider.chars().get(0)).toList()
                )).toList();

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        String content = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new CraftSlideBackgroundsConfig(configs));
        content = Util.removeCharsDoubleSlash(content);

        Path outputFile = this.getOutputFile(BACKGROUNDS_FILE_NAME);
        Files.writeString(outputFile, content);
    }

    private Path getOutputFile(String fileName) {
        return Paths.get(this.getOutputFolder() + File.separator + fileName);
    }

    private Path getOutputFolder() {
        return Paths.get("./" + OUTPUT_FOLDER_NAME);
    }
}
