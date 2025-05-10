package com.github.syr0ws.craftpack.resourcepack.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Pack(@JsonProperty("pack_format") int packFormat,
                   @JsonProperty("description") String description) {
}
