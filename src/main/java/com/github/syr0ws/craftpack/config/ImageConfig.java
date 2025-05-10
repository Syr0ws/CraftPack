package com.github.syr0ws.craftpack.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImageConfig(@JsonProperty(value = "id", required = true) String imageId,
                          @JsonProperty(value = "image", required = true) String image,
                          @JsonProperty(value = "type", defaultValue = "FULLSCREEN") ImageType type) {
}
