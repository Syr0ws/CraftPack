package com.github.syr0ws.craftpack.craftslide;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.syr0ws.craftpack.resourcepack.model.FixedBackground;

import java.util.List;

public record FixedBackgroundConfig(@JsonProperty("background-id") String backgroundId,
                                    @JsonProperty("tile-width") int tileWidth,
                                    @JsonProperty("chars") List<String> chars) implements BackgroundConfig {

    public static FixedBackgroundConfig from(FixedBackground background) {

        List<String> chars = background.providers().stream()
                .map(provider -> provider.chars().get(0))
                .toList();

        return new FixedBackgroundConfig(background.backgroundId(), background.height(), chars);
    }
}
