package com.github.syr0ws.craftpack.craftslide;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.syr0ws.craftpack.resourcepack.model.DynamicBackground;

import java.util.List;

public record DynamicBackgroundConfig(@JsonProperty("background-id") String backgroundId,
                                      @JsonProperty("tile-width") int tileWidth,
                                      @JsonProperty("frames") List<FrameConfig> frames) implements BackgroundConfig {

    public static DynamicBackgroundConfig from(DynamicBackground background) {

        List<FrameConfig> frames = background.frames().stream()
                .map(frame -> new FrameConfig(frame.providers().stream()
                        .map(provider -> provider.chars().get(0))
                        .toList())
                ).toList();

        return new DynamicBackgroundConfig(background.backgroundId(), background.height(), frames);
    }
}
