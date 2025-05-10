package com.github.syr0ws.craftpack.resourcepack;

import com.github.syr0ws.craftpack.config.BackgroundConfig;
import com.github.syr0ws.craftpack.config.ResourcePackConfig;
import com.github.syr0ws.craftpack.resourcepack.model.Background;
import com.github.syr0ws.craftpack.resourcepack.model.CharacterProvider;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BackgroundGenerator extends ImageFontGenerator {

    private static final int BACKGROUND_SIZE = 128;
    private static final int MAX_ROWS = 4;

    private final ResourcePackConfig config;
    private final ResourcePackGenerator.GenerationState state;

    public BackgroundGenerator(ResourcePackConfig config, ResourcePackGenerator.GenerationState state) {
        this.config = config;
        this.state = state;
    }

    public Background generate(BackgroundConfig config, Path backgroundFile, Path outputFolder) throws IOException {

        // Checking that the image is a png file.
        String mimeType = Files.probeContentType(backgroundFile);

        if (!mimeType.equalsIgnoreCase("image/png")) {
            throw new IOException("File '%s' is not a png file");
        }

        BufferedImage image = ImageIO.read(backgroundFile.toFile());

        // Checking that the image can be cut into squared tiles.
        if (image.getHeight() % BACKGROUND_SIZE != 0 || image.getWidth() % BACKGROUND_SIZE != 0) {
            throw new IOException("Background '%s''s height and width must be multiple of %d".formatted(backgroundFile, BACKGROUND_SIZE));
        }

        List<CharacterProvider> providers = new ArrayList<>();

        String fileName = config.backgroundId() + ".png";
        int initialAscent = 16;

        for (int i = 0; i < MAX_ROWS; i++) {

            // Generating the character provider associated with the background.
            int ascent = initialAscent - (i * BACKGROUND_SIZE);
            char character = this.state.getNextChar();

            CharacterProvider provider = super.getProvider(
                    this.config.namespace(), fileName, ascent, BACKGROUND_SIZE, character);

            providers.add(provider);
        }

        // Copying the file into the resource pack.
        Path output = Paths.get(outputFolder + File.separator + fileName);
        Files.copy(backgroundFile, output);

        return new Background(config.backgroundId(), BACKGROUND_SIZE, config.dynamic(), providers);
    }
}
