package io.github.ultimateboomer.smoothboot.config;

import io.github.ultimateboomer.smoothboot.SmoothBoot;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class SmoothBootModMenu implements ModMenuApi {
	@Override
	public String getModId() {
		return SmoothBoot.MOD_ID;
	}
	
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> AutoConfig.getConfigScreen(SmoothBootConfig.class, parent).get();
	}
}
