package com.github.syr0ws.craftpack.craftslide;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ImagesConfig(@JsonProperty("images") List<ImageConfig> images) {

}
