package com.github.syr0ws.craftpack.resourcepack.model;

import com.github.syr0ws.craftpack.config.ImageType;
import com.github.syr0ws.craftpack.util.ImageTile;

import java.util.List;

public record Image(String imageId, List<ImageTile> tiles, ImageType type, int tileHeight, int tileWidth, int rows, int columns, List<CharacterProvider> providers) {

}
