package com.ultimateboomer.smoothboot.config;

import com.ultimateboomer.smoothboot.SmoothBoot;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.BoundedDiscrete;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.PrefixText;

@Config(name = SmoothBoot.MOD_ID)
public class SmoothBootConfig implements ConfigData {
	@PrefixText
	public int serverThreadCount = Runtime.getRuntime().availableProcessors() - 1;
	
	@BoundedDiscrete(min = 1, max = 10)
	@PrefixText
	public int gamePriority = 5;
	
	@BoundedDiscrete(min = 1, max = 10)
	public int serverPriority = 1;
	
	@BoundedDiscrete(min = 1, max = 10)
	public int mainPriority = 1;
}
 