package com.github.syr0ws.craftpack.resourcepack;

import com.github.syr0ws.craftpack.resourcepack.model.Background;
import com.github.syr0ws.craftpack.resourcepack.model.Image;

import java.util.List;

public record ResourcePackGenerationResult(List<Image> images, List<Background> backgrounds) {

}
