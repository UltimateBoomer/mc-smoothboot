package io.github.ultimateboomer.smoothboot;

import io.github.ultimateboomer.smoothboot.config.ConfigHandler;
import io.github.ultimateboomer.smoothboot.config.SmoothBootConfig;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class SmoothBoot implements ModInitializer {
	public static final String MOD_ID = "smoothboot";
	public static final String NAME = "Smooth Boot";
	
	public static final Logger LOGGER = LogManager.getLogger(NAME);
	
	public static SmoothBootConfig config;

	public static boolean initConfig = false;
	public static boolean initBootstrap = false;
	public static boolean initMainWorker = false;
	public static boolean initIOWorker = false;
	
	@Override
	public void onInitialize() {
		
	}
	
	// Called before mod initialization
	public static void regConfig() {
		// Init config
		try {
			config = ConfigHandler.readConfig();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		
		LOGGER.info(NAME + " config initialized");
	}
}
