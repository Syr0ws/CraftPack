package com.github.syr0ws.craftpack.resourcepack.model;

import com.github.syr0ws.craftpack.config.ImageType;

import java.util.List;

public record Image(String imageId, ImageType type, int height, int rows, int columns, List<CharacterProvider> providers) {

}
