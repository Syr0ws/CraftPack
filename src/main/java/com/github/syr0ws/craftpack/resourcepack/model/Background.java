package com.github.syr0ws.craftpack.resourcepack.model;

import java.util.List;

public interface Background {

    String backgroundId();

    int height();

    List<CharacterProvider> providers();
}
