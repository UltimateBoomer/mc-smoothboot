package com.ultimateboomer.laglessboot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ultimateboomer.laglessboot.config.ModConfig;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class LaglessBoot implements ModInitializer {
	public static final String MOD_ID = "laglessboot";
	public static final String NAME = "Lagless Boot";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public static ModConfig config;
	
	@Override
	public void onInitialize() {
		
	}
	
	// Called before mod initialization
	public static void regConfig() {
		// Init config
		AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
		
		LOGGER.info(NAME + " config initialized");
	}
}
