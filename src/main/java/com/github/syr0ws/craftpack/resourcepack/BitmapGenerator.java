package com.github.syr0ws.craftpack.resourcepack;

import com.github.syr0ws.craftpack.config.ImageConfig;
import com.github.syr0ws.craftpack.config.ResourcePackConfig;
import com.github.syr0ws.craftpack.resourcepack.model.CharacterProvider;
import com.github.syr0ws.craftpack.resourcepack.model.Image;
import com.github.syr0ws.craftpack.util.ImageTile;
import com.github.syr0ws.craftpack.util.ImageUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BitmapGenerator extends ImageGenerator {

    private static final int TILE_SIZE = 256;

    private final ResourcePackConfig config;
    private final ResourcePackGenerator.GenerationState state;

    public BitmapGenerator(ResourcePackConfig config, ResourcePackGenerator.GenerationState state) {
        this.config = config;
        this.state = state;
    }

    public Image generate(ImageConfig config, Path imageFile) throws IOException {

        // Checking that the image is a png file.
        String mimeType = Files.probeContentType(imageFile);

        if (!mimeType.equalsIgnoreCase("image/png")) {
            throw new IOException("File '%s' is not a png file");
        }

        BufferedImage image = ImageIO.read(imageFile.toFile());

        // Checking that the image can be cut into squared tiles.
        if(image.getHeight() % TILE_SIZE != 0) {
            throw new IOException("File '%s' has a height which is not a multiple of %d".formatted(imageFile, TILE_SIZE));
        }

        if (image.getWidth() % TILE_SIZE != 0) {
            throw new IOException("File '%s' has a width which is not a multiple of %d".formatted(imageFile, TILE_SIZE));
        }

        // Cutting the image into squared tiles.
        List<ImageTile> tiles = ImageUtil.cutIntoTiles(image, TILE_SIZE);

        // Generating character providers for the resource pack.
        List<CharacterProvider> providers = new ArrayList<>();

        // Dividing by 2 to get a better image quality.
        int charHeight = TILE_SIZE / 2;
        int charWidth = TILE_SIZE / 2;

        int initialAscent = config.type().getInitialAscent();

        for (int i = 0; i < tiles.size(); i++) {

            ImageTile tile = tiles.get(i);

            // Generating the character provider associated with the tile.
            String tileFileName = "%s-%d.png".formatted(config.imageId(), i + 1);
            int ascent = initialAscent - (tile.row() * charHeight);
            char character = this.state.getNextChar();

            CharacterProvider provider = super.createProvider(
                    this.config.namespace(), tileFileName, ascent, charHeight, character);

            providers.add(provider);
        }

        int rows = image.getHeight() / TILE_SIZE;
        int columns = image.getWidth() / TILE_SIZE;

        return new Image(config.imageId(), tiles, config.type(), charHeight, charWidth, rows, columns, providers);
    }
}
