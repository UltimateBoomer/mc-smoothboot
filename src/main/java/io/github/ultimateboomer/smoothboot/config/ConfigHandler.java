package io.github.ultimateboomer.smoothboot.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import io.github.ultimateboomer.smoothboot.SmoothBoot;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigHandler {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
//    public static SmoothBootConfig config;

    public static SmoothBootConfig readConfig() throws IOException {
        String configPath = System.getProperty("user.dir") + "/config/" + SmoothBoot.MOD_ID + ".json";
        SmoothBoot.LOGGER.debug("Config path: " + configPath);

        // Read config
        SmoothBootConfig config;
        try (FileReader reader = new FileReader(configPath)) {
            config = GSON.fromJson(reader, SmoothBootConfig.class);
            if (config == null) {
                throw new NullPointerException();
            }
            config.validate();
            try (FileWriter writer = new FileWriter(configPath)) {
                GSON.toJson(config, writer);
            }

            SmoothBoot.LOGGER.debug("Config: " + config);
        } catch (NullPointerException | JsonParseException | IOException e) {
            // Create new config
            config = new SmoothBootConfig();
            try (FileWriter writer = new FileWriter(configPath)) {
                GSON.toJson(config, writer);
                SmoothBoot.LOGGER.debug("New config file created");
            }
        }
        return config;
    }
}
