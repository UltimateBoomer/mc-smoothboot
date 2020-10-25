package com.ultimateboomer.laglessboot.config;

import com.ultimateboomer.laglessboot.LaglessBoot;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class LaglessBootModMenu implements ModMenuApi {
	@Override
	public String getModId() {
		return LaglessBoot.MOD_ID;
	}
	
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> AutoConfig.getConfigScreen(LaglessBootConfig.class, parent).get();
	}
}
