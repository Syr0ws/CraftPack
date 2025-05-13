package com.github.syr0ws.craftpack;

import com.github.syr0ws.craftpack.config.Config;
import com.github.syr0ws.craftpack.config.ConfigLoader;
import com.github.syr0ws.craftpack.craftslide.ConfigGenerator;
import com.github.syr0ws.craftpack.resourcepack.ResourcePackGenerationResult;
import com.github.syr0ws.craftpack.resourcepack.ResourcePackGenerator;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static Logger LOGGER = Logger.getLogger("CraftPack");

    public static void main(String[] args) {

        try {
            ConfigLoader configLoader = new ConfigLoader();
            configLoader.saveDefaultConfig();

            Config config = configLoader.loadConfig();
            LOGGER.log(Level.INFO, "The configuration has been successfully loaded");

            ResourcePackGenerator resourcePackGenerator = new ResourcePackGenerator(config);
            ResourcePackGenerationResult result = resourcePackGenerator.generate();
            LOGGER.log(Level.INFO, "The resource pack has been successfully generated");

            ConfigGenerator craftSlideConfigGenerator = new ConfigGenerator();
            craftSlideConfigGenerator.generate(result);
            LOGGER.log(Level.INFO, "CraftSlide configuration files have been successfully generated");

        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "An error occurred during the generation", exception);
        }
    }
}