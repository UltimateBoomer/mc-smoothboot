package io.github.ultimateboomer.smoothboot.config;

import io.github.ultimateboomer.smoothboot.SmoothBoot;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.BoundedDiscrete;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.CollapsibleObject;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.Tooltip;

@Config(name = SmoothBoot.MOD_ID)
public class SmoothBootConfig implements ConfigData {
	@CollapsibleObject
	public ThreadCount threadCount = new ThreadCount();

	@CollapsibleObject
	public ThreadPriority threadPriority = new ThreadPriority();

	@CollapsibleObject
	public Optimizations optimizations = new Optimizations();
	
	public static class ThreadCount {
		@Tooltip(count = 2)
		public int main = Math.max(Runtime.getRuntime().availableProcessors() / 2, 1);
	}
	
	public static class ThreadPriority {
		@BoundedDiscrete(min = 1, max = 10)
		public int game = 5;
		
		@BoundedDiscrete(min = 1, max = 10)
		public int main = 3;

		@BoundedDiscrete(min = 1, max = 10)
		public int integratedServer = 5;
	}

	public static class Optimizations {
		@Tooltip
		public boolean dataFixers = true;
	}
}
 