package io.github.ultimateboomer.smoothboot.mixin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

import io.github.ultimateboomer.smoothboot.SmoothBootState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import io.github.ultimateboomer.smoothboot.SmoothBoot;
import io.github.ultimateboomer.smoothboot.config.SmoothBootConfigHandler;

import net.minecraftforge.fml.ModWorkManager;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModWorkManager.class)
public class ModWorkManagerMixin {
	@Inject(method = "newForkJoinWorkerThread(Ljava/util/concurrent/ForkJoinPool;)Ljava/util/concurrent/ForkJoinWorkerThread;", at = @At("HEAD"), cancellable = true, remap = false)
	private static void newForkJoinWorkerThread(ForkJoinPool pool, CallbackInfoReturnable<ForkJoinWorkerThread> ci) {
		if (!SmoothBootState.mcIsRunningDatagen) {
			ForkJoinWorkerThread thread = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
			String workerName = "modloading-worker-" + thread.getPoolIndex();
			SmoothBoot.LOGGER.debug("Initialized " + workerName);
			thread.setName("modloading-worker-" + thread.getPoolIndex());
			thread.setPriority(SmoothBootConfigHandler.config.getModLoadingPriority());
			thread.setContextClassLoader(Thread.currentThread().getContextClassLoader());
			ci.setReturnValue(thread);
		}
	}
}
