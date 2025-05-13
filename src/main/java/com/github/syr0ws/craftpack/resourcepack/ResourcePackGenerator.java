package com.github.syr0ws.craftpack.resourcepack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.syr0ws.craftpack.Main;
import com.github.syr0ws.craftpack.config.BackgroundConfig;
import com.github.syr0ws.craftpack.config.Config;
import com.github.syr0ws.craftpack.config.ImageConfig;
import com.github.syr0ws.craftpack.config.ResourcePackConfig;
import com.github.syr0ws.craftpack.resourcepack.model.*;
import com.github.syr0ws.craftpack.util.ImageTile;
import com.github.syr0ws.craftpack.util.Util;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class ResourcePackGenerator {

    private static final String RESOURCE_PACK_FOLDER_NAME = "resourcepack";
    private static final String IMAGES_FOLDER_NAME = "images";
    private static final String BACKGROUNDS_FOLDER_NAME = "backgrounds";
    private static final String PACK_MCMETA_FILE_NAME = "pack.mcmeta";
    private static final String FONT_PROVIDER_FILE_NAME = "default.json";

    private final Config config;
    private GenerationState generationState;

    public ResourcePackGenerator(Config config) {
        this.config = config;
    }

    public ResourcePackGenerationResult generate() throws IOException {
        this.init();

        // Creating the initial resource pack folders structure.
        this.createResourcePacksFolder();
        this.generateResourcePackStructure(this.config.resourcePack());

        // Images generation.
        List<Image> images = this.generateResourcePackImages();
        List<Background> backgrounds = this.generateResourcePackBackgrounds();

        // Creating the font file.
        List<CharacterProvider> providers = new ArrayList<>();

        images.stream().map(Image::providers).forEach(providers::addAll);
        backgrounds.stream().map(Background::providers).forEach(providers::addAll);

        this.createFontFile(providers);

        return new ResourcePackGenerationResult(images, backgrounds);
    }

    private void init() {
        char initialChar = this.config.initialChar().charAt(0);
        this.generationState = new GenerationState(initialChar);
    }

    private void createResourcePacksFolder() throws IOException {
        Path folder = this.getResourcePacksFolder();
        Files.createDirectories(folder);
    }

    private void generateResourcePackStructure(ResourcePackConfig config) throws IOException {

        Path folder = this.getResourcePacksFolder();
        Path resourcePackFolder = Paths.get(folder + File.separator + config.name());

        // Deleting the resource pack folder and its content if it already exists.
        FileUtils.deleteDirectory(resourcePackFolder.toFile());

        // Creating the initial pack folder structure.
        Path fontFolder = this.getFontFolder(config);
        Path fontTexturesFolder = this.getFontTexturesFolder(config);

        Files.createDirectories(fontFolder);
        Files.createDirectories(fontTexturesFolder);

        // Creating the initial pack files.
        Pack pack = new Pack(config.version(), config.description());
        PackMcMeta mcmeta = new PackMcMeta(pack);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mcmeta);

        Path mcMetaFile = Paths.get(resourcePackFolder + File.separator + PACK_MCMETA_FILE_NAME);
        Files.writeString(mcMetaFile, json);
    }

    private List<Image> generateResourcePackImages() throws IOException {

        Path imagesFolder = this.getImagesFolder();

        if (!Files.isDirectory(imagesFolder)) {
            Main.LOGGER.log(Level.WARNING, "Folder '%s' does not exist. Skipping phase".formatted(imagesFolder));
            return Collections.emptyList();
        }

        ImageGenerator generator = new ImageGenerator(this.config.resourcePack(), this.generationState);
        List<Image> images = new ArrayList<>();

        for (ImageConfig imageConfig : this.config.images()) {

            Path imagePath = this.getImageFile(imageConfig.image());

            if (Files.notExists(imagePath)) {
                Main.LOGGER.log(Level.WARNING, "File '%s' does not exist. Skipping".formatted(imagePath));
                continue;
            }

            Image image = this.generateResourcePackImage(generator, imageConfig, imagePath);
            images.add(image);
        }

        return images;
    }

    private Image generateResourcePackImage(ImageGenerator generator, ImageConfig imageConfig, Path imagePath) throws IOException {

        Image image = generator.generate(imageConfig, imagePath);
        Path outFolder = this.getFontTexturesFolder(this.config.resourcePack());

        // Copying tiles into the resource pack.
        for(int i = 0; i < image.tiles().size(); i++) {

            ImageTile tile = image.tiles().get(i);

            // Copying the file into the resource pack.
            String tileFileName = "%s-%d.png".formatted(imageConfig.imageId(), i+1);

            Path output = Paths.get(outFolder + File.separator + tileFileName);
            ImageIO.write(tile.image(), "png", output.toFile());
        }

        return image;
    }

    private List<Background> generateResourcePackBackgrounds() throws IOException {

        Path backgroundsFolder = this.getBackgroundsFolder();

        if (!Files.isDirectory(backgroundsFolder)) {
            Main.LOGGER.log(Level.WARNING, "Folder '%s' does not exist. Skipping phase".formatted(backgroundsFolder));
            return Collections.emptyList();
        }

        List<Background> backgrounds = new ArrayList<>();

        for (BackgroundConfig backgroundConfig : this.config.backgrounds()) {

            Path backgroundPath = this.getBackgroundFile(backgroundConfig.image());

            if (Files.notExists(backgroundPath)) {
                Main.LOGGER.log(Level.WARNING, "File '%s' does not exist. Skipping".formatted(backgroundPath));
                continue;
            }

            ResourcePackConfig config = this.config.resourcePack();
            Path outFolder = this.getFontTexturesFolder(config);

            BackgroundGenerator generator = new BackgroundGenerator(config, this.generationState);
            Background background = generator.generate(backgroundConfig, backgroundPath, outFolder);

            backgrounds.add(background);
        }

        return backgrounds;
    }

    private void createFontFile(List<CharacterProvider> providers) throws IOException {

        Font font = new Font(providers);

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(font);
        json = Util.removeCharsDoubleSlash(json);

        Path folder = this.getFontFolder(this.config.resourcePack());
        Path fontFile = Paths.get(folder + File.separator + FONT_PROVIDER_FILE_NAME);

        Files.writeString(fontFile, json);
    }

    private Path getResourcePacksFolder() {
        return Paths.get("./" + RESOURCE_PACK_FOLDER_NAME);
    }

    private Path getResourcePackFolder(ResourcePackConfig config) {
        return Paths.get(this.getResourcePacksFolder() + File.separator + config.name());
    }

    private Path getFontFolder(ResourcePackConfig config) {
        return Paths.get(this.getResourcePackFolder(config) + "/assets/" + config.namespace() + "/font");
    }

    private Path getFontTexturesFolder(ResourcePackConfig config) {
        return Paths.get(this.getResourcePackFolder(config) + "/assets/" + config.namespace() + "/textures/font");
    }

    private Path getImageFile(String file) {
        return Paths.get(this.getImagesFolder() + File.separator + file);
    }

    private Path getBackgroundFile(String file) {
        return Paths.get(this.getBackgroundsFolder() + File.separator + file);
    }

    private Path getImagesFolder() {
        return Paths.get("./" + IMAGES_FOLDER_NAME);
    }

    private Path getBackgroundsFolder() {
        return Paths.get("./" + BACKGROUNDS_FOLDER_NAME);
    }

    public static class GenerationState {

        private char currentChar;

        public GenerationState(char currentChar) {
            this.currentChar = currentChar;
        }

        public char getCurrentChar() {
            return this.currentChar;
        }

        public char getNextChar() {

            if(this.currentChar == '\uF8FF') {
                throw new IllegalStateException("Upper bound character \\uF8FF has been reached and there is no character left");
            }

            return this.currentChar++;
        }
    }
}
