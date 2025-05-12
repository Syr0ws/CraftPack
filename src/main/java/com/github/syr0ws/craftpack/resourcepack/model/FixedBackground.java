package com.github.syr0ws.craftpack.resourcepack.model;

import java.util.List;

public record FixedBackground(String backgroundId,
                              int height,
                              List<CharacterProvider> providers) implements Background {

}
