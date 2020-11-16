package com.ultimateboomer.smoothboot.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ultimateboomer.smoothboot.SmoothBoot;

import net.minecraft.client.main.Main;

@Mixin(Main.class)
public class MainMixin {
	@Inject(method = "main", at = @At("HEAD"), remap = false)
	private static void onMain(CallbackInfo ci) {
		SmoothBoot.LOGGER.debug("Current thread: " + Thread.currentThread());
		Thread.currentThread().setPriority(1);
		SmoothBoot.LOGGER.debug("Client Thread Priority Replaced");
	}
}
