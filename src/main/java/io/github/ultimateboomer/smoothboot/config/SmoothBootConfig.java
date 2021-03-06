package io.github.ultimateboomer.smoothboot.config;

import io.github.ultimateboomer.smoothboot.SmoothBoot;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Config(name = SmoothBoot.MOD_ID)
public class SmoothBootConfig implements ConfigData {
	@ConfigEntry.Gui.CollapsibleObject
	public ThreadCount threadCount = new ThreadCount();

	@ConfigEntry.Gui.CollapsibleObject
	public ThreadPriority threadPriority = new ThreadPriority();
	
	public static class ThreadCount {
		@ConfigEntry.Gui.Tooltip(count = 2)
		public int bootstrap = 1;

		@ConfigEntry.Gui.Tooltip(count = 2)
		public int main = Math.max(Runtime.getRuntime().availableProcessors() - 2, 1);
	}
	
	public static class ThreadPriority {
		@ConfigEntry.BoundedDiscrete(min = 1, max = 10)
		public int game = 5;
		
		@ConfigEntry.BoundedDiscrete(min = 1, max = 10)
		public int bootstrap = 1;
		
		@ConfigEntry.BoundedDiscrete(min = 1, max = 10)
		public int main = 1;
		
		@ConfigEntry.BoundedDiscrete(min = 1, max = 10)
		public int io = 1;

		@Environment(EnvType.CLIENT)
		@ConfigEntry.BoundedDiscrete(min = 1, max = 10)
		public int integratedServer = 5;
	}
}
 