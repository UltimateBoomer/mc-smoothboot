package com.ultimateboomer.smoothboot.config;

import java.util.function.Function;

import com.ultimateboomer.smoothboot.SmoothBoot;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public class SmoothBootModMenu implements ModMenuApi {
	@Override
	public String getModId() {
		return SmoothBoot.MOD_ID;
	}
	
	@Override
	public Function<Screen, ? extends Screen> getConfigScreenFactory() {
		return parent -> AutoConfig.getConfigScreen(SmoothBootConfig.class, parent).get();
	}
}
