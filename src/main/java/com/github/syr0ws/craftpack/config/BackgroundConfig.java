package com.github.syr0ws.craftpack.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BackgroundConfig(@JsonProperty(value = "id", required = true) String backgroundId,
                               @JsonProperty(value = "image", required = true) String image,
                               @JsonProperty(value = "dynamic", defaultValue = "false") boolean dynamic) {
}
