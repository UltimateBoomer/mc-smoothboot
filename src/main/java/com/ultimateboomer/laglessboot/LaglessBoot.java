package com.ultimateboomer.laglessboot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ultimateboomer.laglessboot.config.LaglessBootConfig;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class LaglessBoot implements ClientModInitializer {
	public static final String MOD_ID = "laglessboot";
	public static final String NAME = "Lagless Boot";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public static LaglessBootConfig config;
	
	// Too late for reading config
	@Override
	public void onInitializeClient() {
		
	}
	
	// Called before mod initialization
	public static void regConfig() {
		// Init config
		AutoConfig.register(LaglessBootConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(LaglessBootConfig.class).getConfig();
		
		LOGGER.info(NAME + " config initialized");
	}
}
