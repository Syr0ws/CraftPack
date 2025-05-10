package com.github.syr0ws.craftpack.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Config(@JsonProperty(value = "initial-char", required = true) String initialChar,
                     @JsonProperty(value = "resource-pack", required = true) ResourcePackConfig resourcePack,
                     @JsonProperty(value = "backgrounds", required = true) List<BackgroundConfig> backgrounds,
                     @JsonProperty(value = "images", required = true) List<ImageConfig> images) {
}
