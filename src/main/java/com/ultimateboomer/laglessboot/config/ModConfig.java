package com.ultimateboomer.laglessboot.config;

import com.ultimateboomer.laglessboot.LaglessBoot;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = LaglessBoot.MOD_ID)
public class ModConfig implements ConfigData {
	@ConfigEntry.BoundedDiscrete(min = 1, max = Integer.MAX_VALUE)
	public int threadCount = 1;
}
