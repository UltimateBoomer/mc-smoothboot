package com.ultimateboomer.smoothboot.mixin;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.ultimateboomer.smoothboot.SmoothBoot;
import com.ultimateboomer.smoothboot.config.SmoothBootConfig.SmoothBootPerformance;

import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

@Mixin(Util.class)
public abstract class UtilMixin {
	private static boolean initConfig = false;
	private static boolean initBootstrapExecutor = false;
	private static boolean initMainWorkerExecutor = false;
	
	@Shadow
	private static ExecutorService BOOTSTRAP_EXECUTOR;
	
	@Shadow
	private static ExecutorService MAIN_WORKER_EXECUTOR;
	
	@Shadow
	@Final
	private static AtomicInteger NEXT_WORKER_ID;
	
	@Shadow
	@Final
	private static Logger LOGGER;
	
	@Shadow
	protected static void method_18347(Thread thread, Throwable throwable) {};
	
	// Probably not ideal, but this is the only way I found to modify createWorker without causing errors.
	// Redirecting or overwriting causes static initialization to be called too early resulting in NullPointerException being thrown.
	@Overwrite()
	public static Executor getBootstrapExecutor() {
		if (!initBootstrapExecutor) {
			BOOTSTRAP_EXECUTOR = replWorker("Bootstrap");
			initBootstrapExecutor = true;
		}
		return BOOTSTRAP_EXECUTOR;
	}
	
	@Overwrite()
	public static Executor getMainWorkerExecutor() {
		if (!initMainWorkerExecutor) {
			MAIN_WORKER_EXECUTOR = replWorker("Main");
			initMainWorkerExecutor = true;
		}
		return MAIN_WORKER_EXECUTOR;
	}
	
	// Replace createWorker
	private static ExecutorService replWorker(String name) {
		if (!initConfig) {
			SmoothBoot.regConfig();
			initConfig = true;
		}
		SmoothBoot.LOGGER.info("Initialized " + name + " worker");
		SmoothBootPerformance perf = SmoothBoot.config.getPerformance();
		Object executorService2 = new ForkJoinPool(MathHelper.clamp(select(name, perf.bootstrapThreadCount, perf.mainThreadCount),
			1, 0x7fff), (forkJoinPool) -> {
			ForkJoinWorkerThread forkJoinWorkerThread = new ForkJoinWorkerThread(forkJoinPool) {
				protected void onTermination(Throwable throwable) {
					if (throwable != null) {
						LOGGER.warn("{} died", this.getName(), throwable);
					} else {
						LOGGER.debug("{} shutdown", this.getName());
					}

					super.onTermination(throwable);
				}
			};
			forkJoinWorkerThread.setPriority(select(name, perf.bootstrapPriority, perf.mainPriority));
			forkJoinWorkerThread.setName("Worker-" + name + "-" + NEXT_WORKER_ID.getAndIncrement());
			return forkJoinWorkerThread;
		}, UtilMixin::method_18347, true);
		SmoothBoot.LOGGER.info(executorService2);
		return (ExecutorService) executorService2;
	}
	
	private static <T> T select(String name, T bootstrap, T main) {
		return name == "Bootstrap" ? bootstrap : main;
	}
}
