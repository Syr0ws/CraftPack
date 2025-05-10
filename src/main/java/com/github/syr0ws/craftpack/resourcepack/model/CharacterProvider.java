package com.github.syr0ws.craftpack.resourcepack.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CharacterProvider(
        @JsonProperty("type") String type,
        @JsonProperty("file") String file,
        @JsonProperty("ascent") int ascent,
        @JsonProperty("height") int height,
        @JsonProperty("chars") List<String> chars) {
}
