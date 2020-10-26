package com.ultimateboomer.smoothboot.config;

import com.ultimateboomer.smoothboot.SmoothBoot;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import net.minecraft.util.math.MathHelper;

@Config(name = SmoothBoot.MOD_ID)
public class SmoothBootConfig implements ConfigData {
	@ConfigEntry.Gui.Tooltip
	public int threadCount = MathHelper.clamp(Runtime.getRuntime().availableProcessors() / 2 - 1, 1, 7);
	
	@ConfigEntry.BoundedDiscrete(min = Thread.MIN_PRIORITY, max = Thread.MAX_PRIORITY)
	@ConfigEntry.Gui.Tooltip
	public int priority = Thread.MIN_PRIORITY;
}
