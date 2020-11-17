package com.ultimateboomer.smoothboot.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ultimateboomer.smoothboot.SmoothBoot;
import com.ultimateboomer.smoothboot.SmoothBootState;

import net.minecraft.client.main.Main;

@Mixin(Main.class)
public class MainMixin {
	@Inject(method = "main", at = @At("HEAD"), remap = false)
	private static void onMain(CallbackInfo ci) {
		if (!SmoothBootState.initConfig) {
			SmoothBoot.regConfig();
			SmoothBootState.initConfig = true;
		}
		
		Thread.currentThread().setPriority(SmoothBoot.config.gamePriority);
		SmoothBoot.LOGGER.debug("Initialized client game thread");
	}
}
