package com.ultimateboomer.smoothboot.config;

import com.ultimateboomer.smoothboot.SmoothBoot;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.BoundedDiscrete;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.CollapsibleObject;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.EnumHandler;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.EnumHandler.EnumDisplayOption;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.Excluded;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.Tooltip;
import net.minecraft.util.math.MathHelper;

@Config(name = SmoothBoot.MOD_ID)
public class SmoothBootConfig implements ConfigData {
	@Excluded
	private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();
	
	@EnumHandler(option = EnumDisplayOption.BUTTON)
	@Tooltip(count = 2)
	public SmoothBootPreset preset = SmoothBootPreset.MEDIUM;
	
	@CollapsibleObject
	public SmoothBootPerformance custom = new SmoothBootPerformance();
	
	public SmoothBootPerformance getPerformance() {
		if (preset != SmoothBootPreset.CUSTOM) {
			return preset.performance;
		} else {
			return custom;
		}
	}
	
	public static enum SmoothBootPreset {
		MINIMUM(new SmoothBootPerformance(1, 1, 1, 1)),
		LOW(new SmoothBootPerformance(1, PROCESSORS / 2 - 1, 1, 1)),
		MEDIUM(new SmoothBootPerformance(1, PROCESSORS - 1, 1, 1)),
		HIGH(new SmoothBootPerformance(1, PROCESSORS - 1, 5, 5)),
		ULTRA(new SmoothBootPerformance(1, PROCESSORS - 1, 10, 10)),
		CUSTOM(null);
		
		private SmoothBootPreset(SmoothBootPerformance performance) {
			this.performance = performance;
		}
		
		public SmoothBootPerformance performance;
	}
	
	public static class SmoothBootPerformance {
		public SmoothBootPerformance() {
			this(1, 1, 1, 1);
		}
		
		public SmoothBootPerformance(int bootstrapThreadCount, int mainThreadCount, int bootstrapPriority, int mainPriority) {
			this.bootstrapThreadCount = Math.max(1, bootstrapThreadCount);
			this.mainThreadCount = Math.max(1, mainThreadCount);
			this.bootstrapPriority = MathHelper.clamp(bootstrapPriority, 1, 10);
			this.mainPriority = mainPriority;
		}
		
		@Tooltip
		public int bootstrapThreadCount;
		
		@Tooltip
		public int mainThreadCount;
		
		@BoundedDiscrete(min = 1, max = 10)
		@Tooltip
		public int bootstrapPriority;
		
		@BoundedDiscrete(min = 1, max = 10)
		@Tooltip
		public int mainPriority;
	}
}
 