package com.github.syr0ws.craftpack.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResourcePackConfig(@JsonProperty(value = "name", required = true) String name,
                                 @JsonProperty(value = "version", required = true) int version,
                                 @JsonProperty(value = "description", required = true) String description,
                                 @JsonProperty(value = "namespace", required = true) String namespace) {
}
