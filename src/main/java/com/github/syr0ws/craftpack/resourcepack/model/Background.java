package com.github.syr0ws.craftpack.resourcepack.model;

import java.util.List;

public record Background(String backgroundId, int height, boolean dynamic, List<CharacterProvider> providers) {

}
