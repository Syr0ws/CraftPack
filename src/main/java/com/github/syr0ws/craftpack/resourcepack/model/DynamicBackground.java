package com.github.syr0ws.craftpack.resourcepack.model;

import java.util.List;

public record DynamicBackground(String backgroundId,
                                int height,
                                int step,
                                List<Frame> frames) implements Background {

    @Override
    public List<CharacterProvider> providers() {
        return this.frames.stream().flatMap(frame -> frame.providers().stream()).toList();
    }
}
