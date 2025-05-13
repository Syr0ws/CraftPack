package com.github.syr0ws.craftpack.craftslide;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record BackgroundsConfig(@JsonProperty("backgrounds") List<FixedBackgroundConfig> backgrounds) {

}
