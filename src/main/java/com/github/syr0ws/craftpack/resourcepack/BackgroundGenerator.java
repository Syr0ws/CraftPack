package com.github.syr0ws.craftpack.resourcepack;

import com.github.syr0ws.craftpack.config.BackgroundConfig;
import com.github.syr0ws.craftpack.config.ResourcePackConfig;
import com.github.syr0ws.craftpack.resourcepack.model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BackgroundGenerator extends BitmapGenerator {

    private static final int BACKGROUND_SIZE = 128;
    private static final int MAX_ROWS = 4;

    private final ResourcePackConfig config;
    private final ResourcePackGenerator.GenerationState state;

    public BackgroundGenerator(ResourcePackConfig config, ResourcePackGenerator.GenerationState state) {
        this.config = config;
        this.state = state;
    }

    public Background generate(BackgroundConfig config, Path backgroundFile) throws IOException {

        // Checking that the image is a png file.
        String mimeType = Files.probeContentType(backgroundFile);

        if (!mimeType.equalsIgnoreCase("image/png")) {
            throw new IOException("File '%s' is not a png file");
        }

        BufferedImage image = ImageIO.read(backgroundFile.toFile());

        // Checking that the image can be cut into squared tiles.
        if (image.getHeight() % BACKGROUND_SIZE != 0) {
            throw new IOException("File '%s' has a height which is not a multiple of %d".formatted(backgroundFile, BACKGROUND_SIZE));
        }

        if (image.getWidth() % BACKGROUND_SIZE != 0) {
            throw new IOException("File '%s' has a width which is not a multiple of %d".formatted(backgroundFile, BACKGROUND_SIZE));
        }

        // Generating the background.
        return config.dynamic() ? this.generateDynamicBackground(config) : this.generateFixedBackground(config);
    }

    private Background generateFixedBackground(BackgroundConfig config) {

        List<CharacterProvider> providers = new ArrayList<>();
        int initialAscent = 16;

        for (int i = 0; i < MAX_ROWS; i++) {

            // Generating the character provider associated with the background.
            String fileName = config.backgroundId() + ".png";
            int ascent = initialAscent - (i * BACKGROUND_SIZE);
            char character = this.state.getNextChar();

            CharacterProvider provider = super.createProvider(
                    this.config.namespace(), fileName, ascent, BACKGROUND_SIZE, character);

            providers.add(provider);
        }

        return new FixedBackground(config.backgroundId(), BACKGROUND_SIZE, providers);
    }

    private Background generateDynamicBackground(BackgroundConfig config) {

        List<Frame> frames = new ArrayList<>();
        String fileName = config.backgroundId() + ".png";

        for (int ascent = 0; ascent < 512; ascent += config.step()) {

            List<CharacterProvider> providers = new ArrayList<>();

            for (int row = 0; row < MAX_ROWS; row++) {

                char character = this.state.getNextChar();

                CharacterProvider provider = super.createProvider(
                        this.config.namespace(), fileName, ascent, BACKGROUND_SIZE, character);

                providers.add(provider);
            }

            frames.add(new Frame(providers));
        }

        return new DynamicBackground(config.backgroundId(), BACKGROUND_SIZE, config.step(), frames);
    }
}
