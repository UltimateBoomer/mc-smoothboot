package com.ultimateboomer.smoothboot.mixin;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.ultimateboomer.smoothboot.LoggingForkJoinWorkerThread;
import com.ultimateboomer.smoothboot.SmoothBoot;

import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

@Mixin(Util.class)
public abstract class UtilMixin {
	private static boolean initConfig = false;
	private static boolean initBootstrap = false;
	private static boolean initMainWorker = false;
	private static boolean initIOWorker = false;
	
	@Shadow
	private static ExecutorService BOOTSTRAP_EXECUTOR;
	
	@Shadow
	private static ExecutorService MAIN_WORKER_EXECUTOR;
	
	@Shadow
	private static ExecutorService IO_WORKER_EXECUTOR;
	
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
		if (!initBootstrap) {
			BOOTSTRAP_EXECUTOR = replWorker("Bootstrap");
			SmoothBoot.LOGGER.debug("Bootstrap worker replaced");
			initBootstrap = true;
		}
		return BOOTSTRAP_EXECUTOR;
	}
	
	@Overwrite()
	public static Executor getMainWorkerExecutor() {
		if (!initMainWorker) {
			MAIN_WORKER_EXECUTOR = replWorker("Main");
			SmoothBoot.LOGGER.debug("Main worker replaced");
			initMainWorker = true;
		}
		return MAIN_WORKER_EXECUTOR;
	}
	
	@Overwrite
	public static Executor getIoWorkerExecutor() {
		if (!initIOWorker) {
			IO_WORKER_EXECUTOR = replIoWorker();
			SmoothBoot.LOGGER.debug("IO worker replaced");
			initIOWorker = true;
		}
		return IO_WORKER_EXECUTOR;
	}
	
	// Replace createWorker
	private static ExecutorService replWorker(String name) {
		if (!initConfig) {
			SmoothBoot.regConfig();
			initConfig = true;
		}
		
		ExecutorService executorService2 = new ForkJoinPool(MathHelper.clamp(select(name, SmoothBoot.config.bootstrapThreadCount,
			SmoothBoot.config.mainThreadCount), 1, 0x7fff), (forkJoinPool) -> {
				String workerName = "Worker-" + name + "-" + NEXT_WORKER_ID.getAndIncrement();
				SmoothBoot.LOGGER.debug("Initialized " + workerName);
				
				ForkJoinWorkerThread forkJoinWorkerThread = new LoggingForkJoinWorkerThread(forkJoinPool, LOGGER);
				forkJoinWorkerThread.setPriority(select(name, SmoothBoot.config.bootstrapPriority, SmoothBoot.config.mainPriority));
				forkJoinWorkerThread.setName(workerName);
				return forkJoinWorkerThread;
		}, UtilMixin::method_18347, true);
		SmoothBoot.LOGGER.info(executorService2);
		return executorService2;
	}
	
	// Replace createIoWorker
	private static ExecutorService replIoWorker() {
		return Executors.newCachedThreadPool((runnable) -> {
			String workerName = "IO-Worker-" + NEXT_WORKER_ID.getAndIncrement();
			SmoothBoot.LOGGER.debug("Initialized " + workerName);
			
			Thread thread = new Thread(runnable);
			thread.setName(workerName);
			thread.setDaemon(true);
			thread.setPriority(SmoothBoot.config.ioPriority);
			thread.setUncaughtExceptionHandler(UtilMixin::method_18347);
			return thread;
		});
	}
	
	private static <T> T select(String name, T bootstrap, T main) {
		return Objects.equals(name, "Bootstrap") ? bootstrap : main;
	}
}
