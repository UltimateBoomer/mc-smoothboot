package io.github.ultimateboomer.smoothboot;

import io.github.ultimateboomer.smoothboot.config.SmoothBootConfig;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SmoothBoot implements ModInitializer {
	public static final String MOD_ID = "smoothboot";
	public static final String NAME = "Smooth Boot";
	
	public static final Logger LOGGER = LogManager.getLogger(NAME);
	
	public static SmoothBootConfig config;
	
	@Override
	public void onInitialize() {
		
	}
	
	// Called before mod initialization
	public static void regConfig() {
		// Init config
		AutoConfig.register(SmoothBootConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(SmoothBootConfig.class).getConfig();
		
		LOGGER.info(NAME + " config initialized");
	}
}
