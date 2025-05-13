package com.github.syr0ws.craftpack.craftslide;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.syr0ws.craftpack.resourcepack.model.Image;

import java.util.List;

public record ImageConfig(@JsonProperty("image-id") String imageId,
                          @JsonProperty("tile-width") int tileWidth,
                          @JsonProperty("rows") int rows,
                          @JsonProperty("columns") int columns,
                          @JsonProperty("chars") List<String> chars) {

    public static ImageConfig from(Image image) {

        List<String> chars = image.providers().stream()
                .map(provider -> provider.chars().get(0))
                .toList();

        return new ImageConfig(image.imageId(), image.tileWidth(), image.rows(), image.columns(), chars);
    }
}
