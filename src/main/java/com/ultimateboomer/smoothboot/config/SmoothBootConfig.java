package com.ultimateboomer.smoothboot.config;

import com.ultimateboomer.smoothboot.SmoothBoot;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.BoundedDiscrete;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.Tooltip;

@Config(name = SmoothBoot.MOD_ID)
public class SmoothBootConfig implements ConfigData {
	@Tooltip
	public int serverThreadCount = Runtime.getRuntime().availableProcessors() - 1;
	
	@Tooltip
	public int mainThreadCount = Runtime.getRuntime().availableProcessors() - 1;
	
	@BoundedDiscrete(min = 1, max = 10)
	@Tooltip
	public int gamePriority = 5;
	
	@BoundedDiscrete(min = 1, max = 10)
	@Tooltip
	public int mainPriority = 1;
	
	@BoundedDiscrete(min = 1, max = 10)
	@Tooltip
	public int serverPriority = 1;
}
 