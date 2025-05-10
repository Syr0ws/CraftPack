package com.github.syr0ws.craftpack.config;

public enum ImageType {

    FULLSCREEN(16), SCREEN_CENTERED(-16);

    private final int initialAscent;

    ImageType(int initialAscent) {
        this.initialAscent = initialAscent;
    }

    public int getInitialAscent() {
        return this.initialAscent;
    }
}
