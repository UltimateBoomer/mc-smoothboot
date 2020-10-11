package com.ultimateboomer.laglessboot.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.ultimateboomer.laglessboot.LaglessBoot;

import net.minecraft.util.Util;

@Mixin(Util.class)
public class UtilMixin {
	private static boolean init = false;
	
	@Redirect(method = "createWorker", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I"))
	private static int setThreads(int value, int min, int max) {
		if (!init) {
			LaglessBoot.regConfig();
			init = true;
		}
		// Replace thread count
		LaglessBoot.LOGGER.info("Threadcount replaced: " + LaglessBoot.config.threadCount);
		return LaglessBoot.config.threadCount;
	}
}
