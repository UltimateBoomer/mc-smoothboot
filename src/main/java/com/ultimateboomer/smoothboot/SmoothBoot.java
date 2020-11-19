package com.ultimateboomer.smoothboot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ultimateboomer.smoothboot.config.SmoothBootConfigHandler;

import net.minecraftforge.fml.common.Mod;

@Mod(SmoothBoot.MOD_ID)
public class SmoothBoot {
	public static final String MOD_ID = "smoothboot";
	public static final String MOD_NAME = "Smooth Boot";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

	public SmoothBoot() {
		// Check if mixin is loaded, config must be initalized by this point if mixin works
		if (SmoothBootConfigHandler.config == null) {
			throw new IllegalStateException("Mixin not loaded! Make sure MixinBootstrap is installed.");
		}
	}
}
