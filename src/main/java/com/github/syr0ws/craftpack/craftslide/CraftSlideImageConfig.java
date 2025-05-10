package com.github.syr0ws.craftpack.craftslide;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CraftSlideImageConfig(@JsonProperty("image-id") String imageId,
                                    @JsonProperty("height") int height,
                                    @JsonProperty("rows") int rows,
                                    @JsonProperty("columns") int columns,
                                    @JsonProperty("chars") List<String> chars) {
}
