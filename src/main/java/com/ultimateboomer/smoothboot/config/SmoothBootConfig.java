package com.ultimateboomer.smoothboot.config;

import com.ultimateboomer.smoothboot.SmoothBoot;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.BoundedDiscrete;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.PrefixText;

@Config(name = SmoothBoot.MOD_ID)
public class SmoothBootConfig implements ConfigData {
	@PrefixText
	public int bootstrapThreadCount = 1;
	
	public int mainThreadCount = Math.min(Runtime.getRuntime().availableProcessors() - 1, 7);
	
	@PrefixText
	@BoundedDiscrete(min = 1, max = 10)
	public int gamePriority = 5;
	
	@BoundedDiscrete(min = 1, max = 10)
	public int bootstrapPriority = 1;
	
	@BoundedDiscrete(min = 1, max = 10)
	public int mainPriority = 1;
	
	@BoundedDiscrete(min = 1, max = 10)
	public int ioPriority = 5;
}
 