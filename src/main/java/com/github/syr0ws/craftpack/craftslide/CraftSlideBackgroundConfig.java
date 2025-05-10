package com.github.syr0ws.craftpack.craftslide;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CraftSlideBackgroundConfig(@JsonProperty("background-id") String backgroundId,
                                         @JsonProperty("height") int height,
                                         @JsonProperty("chars") List<String> chars) {
}
