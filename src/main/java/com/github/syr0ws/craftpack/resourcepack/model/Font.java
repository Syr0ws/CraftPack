package com.github.syr0ws.craftpack.resourcepack.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Font(@JsonProperty("providers") List<CharacterProvider> providers) {

}
