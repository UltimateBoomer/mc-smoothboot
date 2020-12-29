package io.github.ultimateboomer.smoothboot.mixin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

import io.github.ultimateboomer.smoothboot.SmoothBoot;
import io.github.ultimateboomer.smoothboot.config.SmoothBootConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraftforge.fml.ModList;

@Mixin(ModList.class)
public class ModListMixin {
	@Overwrite
	private static ForkJoinWorkerThread newForkJoinWorkerThread(ForkJoinPool pool) {
		ForkJoinWorkerThread thread = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
		String workerName = "modloading-worker-" + thread.getPoolIndex();
		SmoothBoot.LOGGER.debug("Initialized " + workerName);
		thread.setName("modloading-worker-" + thread.getPoolIndex());
		thread.setPriority(SmoothBootConfigHandler.config.getModLoadingPriority());
		thread.setContextClassLoader(Thread.currentThread().getContextClassLoader());
		return thread;
	}
}
