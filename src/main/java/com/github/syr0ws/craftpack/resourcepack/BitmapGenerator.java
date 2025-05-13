package com.github.syr0ws.craftpack.resourcepack;

import com.github.syr0ws.craftpack.resourcepack.model.CharacterProvider;
import com.github.syr0ws.craftpack.util.Constants;

import java.util.List;

public abstract class BitmapGenerator {

    protected CharacterProvider createProvider(String namespace, String fileName, int ascent, int height, char c) {

        String file = "%s:font/%s".formatted(namespace, fileName);
        String unicode = String.format("\\u%04x", (int) c);

        return new CharacterProvider(Constants.BITMAP, file, ascent, height, List.of(unicode));
    }
}
