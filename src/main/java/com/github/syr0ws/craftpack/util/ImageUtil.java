package com.github.syr0ws.craftpack.util;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImageUtil {

    public static List<ImageTile> cutIntoTiles(BufferedImage image, int tileSize) {

        int height = image.getHeight();
        int width = image.getWidth();

        if(height % tileSize != 0) {
            throw new IllegalArgumentException("Image height %d is not divisible by the tile size %d".formatted(height, tileSize));
        }

        if (width % tileSize != 0) {
            throw new IllegalArgumentException("Image width %d is not divisible by the tile size %d".formatted(height, tileSize));
        }

        int rows = height / tileSize;
        int columns = width / tileSize;

        List<ImageTile> tiles = new ArrayList<>(rows * columns);

        for (int row = 0; row < rows; row++) {

            for (int column = 0; column < columns; column++) {

                // x is for columns and y for rows so values are reversed here.
                BufferedImage tile = image.getSubimage(column * tileSize, row * tileSize, tileSize, tileSize);
                tiles.add(new ImageTile(tile, row, column));
            }
        }

        return tiles;
    }
}
