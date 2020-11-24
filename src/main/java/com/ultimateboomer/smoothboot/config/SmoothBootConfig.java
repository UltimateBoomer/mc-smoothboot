package com.ultimateboomer.smoothboot.config;

import com.ultimateboomer.smoothboot.SmoothBoot;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.BoundedDiscrete;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.PrefixText;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.Tooltip;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.TransitiveObject;

@Config(name = SmoothBoot.MOD_ID)
public class SmoothBootConfig implements ConfigData {
	@PrefixText
	@TransitiveObject
	public ThreadCount threadCount = new ThreadCount();
	
	@PrefixText
	@TransitiveObject
	public ThreadPriority threadPriority = new ThreadPriority();
	
	public static class ThreadCount {
		@Tooltip(count = 2)
		public int server = Math.max(Runtime.getRuntime().availableProcessors() / 2, 1);
	}
	
	public static class ThreadPriority {
		@BoundedDiscrete(min = 1, max = 10)
		public int game = 5;
		
		@BoundedDiscrete(min = 1, max = 10)
		public int server = 3;
		
		@BoundedDiscrete(min = 1, max = 10)
		public int main = 3;
	}
}
 