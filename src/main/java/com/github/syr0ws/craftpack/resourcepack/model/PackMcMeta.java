package com.github.syr0ws.craftpack.resourcepack.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PackMcMeta(@JsonProperty("pack") Pack pack) {
}
