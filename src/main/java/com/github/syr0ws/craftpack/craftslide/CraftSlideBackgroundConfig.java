package com.github.syr0ws.craftpack.craftslide;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CraftSlideBackgroundConfig(@JsonProperty("background-id") String backgroundId,
                                         @JsonProperty("tile-width") int tileWidth,
                                         @JsonProperty("chars") List<String> chars) {
}
