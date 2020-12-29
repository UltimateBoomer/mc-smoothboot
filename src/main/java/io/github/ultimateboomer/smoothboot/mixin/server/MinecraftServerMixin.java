package io.github.ultimateboomer.smoothboot.mixin.server;

import java.io.IOException;

import io.github.ultimateboomer.smoothboot.SmoothBoot;
import io.github.ultimateboomer.smoothboot.SmoothBootState;
import io.github.ultimateboomer.smoothboot.config.SmoothBootConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@Inject(method = "main", at = @At("HEAD"), remap = false)
	private static void onMain(CallbackInfo ci) throws IOException {
		if (!SmoothBootState.initConfig) {
			SmoothBootConfigHandler.readConfig();
			SmoothBootState.initConfig = true;
		}
		
		Thread.currentThread().setPriority(SmoothBootConfigHandler.config.getGamePriority());
		SmoothBoot.LOGGER.debug("Initialized client game thread");
	}
}
