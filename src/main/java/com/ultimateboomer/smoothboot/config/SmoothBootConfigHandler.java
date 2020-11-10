package com.ultimateboomer.smoothboot.config;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.ultimateboomer.smoothboot.SmoothBoot;

public class SmoothBootConfigHandler {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	public static SmoothBootConfig config;
	
	public static void readConfig() {
		String configPath = System.getProperty("user.dir") + "\\config\\" + SmoothBoot.MOD_ID + ".json";
		SmoothBoot.LOGGER.info("Config path: " + configPath);
		
		try (FileReader reader = new FileReader(configPath)) {
			config = GSON.fromJson(reader, SmoothBootConfig.class);
			if (config == null) {
				throw new NullPointerException();
			}
			SmoothBoot.LOGGER.info("Config: " + config);
		} catch (NullPointerException | JsonParseException | IOException e) {
			config = newConfig();
			try (FileWriter writer = new FileWriter(configPath)) {
				GSON.toJson(config, writer);
				SmoothBoot.LOGGER.info("New config file created");
			} catch (JsonIOException | IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private static SmoothBootConfig newConfig() {
		SmoothBootConfig config = new SmoothBootConfig();
		config.setBootstrapThreads(1);
		config.setMainThreads(Runtime.getRuntime().availableProcessors() - 1);
		config.setBootstrapPriority(1);
		config.setMainPriority(1);
		config.setIoPriority(5);
		config.setModLoadingPriority(1);
		return config;
	}
}
