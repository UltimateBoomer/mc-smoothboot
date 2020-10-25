package com.ultimateboomer.laglessboot.mixin;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.ultimateboomer.laglessboot.LaglessBoot;

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
	@org.spongepowered.asm.mixin.Overwrite()
	public static Executor getBootstrapExecutor() {
		if (!initBootstrapExecutor) {
			BOOTSTRAP_EXECUTOR = replWorker("Bootstrap");
			initBootstrapExecutor = true;
		}
		return BOOTSTRAP_EXECUTOR;
	}
	
	@org.spongepowered.asm.mixin.Overwrite()
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
			LaglessBoot.regConfig();
			initConfig = true;
		}
		
		LaglessBoot.LOGGER.info("Initialized " + name);
		Object executorService2 = new ForkJoinPool(MathHelper.clamp(LaglessBoot.config.threadCount, 1, 0x7fff), (forkJoinPool) -> {
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
			forkJoinWorkerThread.setPriority(LaglessBoot.config.priority);
			forkJoinWorkerThread.setName("Worker-" + name + "-" + NEXT_WORKER_ID.getAndIncrement());
			return forkJoinWorkerThread;
		}, UtilMixin::method_18347, true);
		return (ExecutorService) executorService2;
	}
}
