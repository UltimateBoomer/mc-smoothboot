package io.github.ultimateboomer.smoothboot.mixin;

import io.github.ultimateboomer.smoothboot.SmoothBoot;
import io.github.ultimateboomer.smoothboot.SmoothBootState;
import io.github.ultimateboomer.smoothboot.util.LoggingForkJoinWorkerThread;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(Util.class)
public abstract class UtilMixin {
	@Shadow
	private static ExecutorService BOOTSTRAP;
	
	@Shadow
	private static ExecutorService SERVER_WORKER_EXECUTOR;
	
	@Shadow
	private static ExecutorService field_24477;
	
	@Shadow
	@Final
	private static AtomicInteger NEXT_SERVER_WORKER_ID;
	
	@Shadow
	@Final
	private static Logger LOGGER;
	
	@Shadow
	protected static void method_18347(Thread thread, Throwable throwable) {};
	
	// Probably not ideal, but this is the only way I found to modify createWorker without causing errors.
	// Redirecting or overwriting causes static initialization to be called too early resulting in NullPointerException being thrown.
	@Overwrite(remap = false)
	public static Executor method_28124() {
		if (!SmoothBootState.initBootstrap) {
			BOOTSTRAP = replWorker("Bootstrap");
			SmoothBoot.LOGGER.debug("Bootstrap worker replaced");
			SmoothBootState.initBootstrap = true;
		}
		return BOOTSTRAP;
	}
	
	@Overwrite()
	public static Executor getServerWorkerExecutor() {
		if (!SmoothBootState.initMainWorker) {
			SERVER_WORKER_EXECUTOR = replWorker("Main");
			SmoothBoot.LOGGER.debug("Main worker replaced");
			SmoothBootState.initMainWorker = true;
		}
		return SERVER_WORKER_EXECUTOR;
	}
	
	@Overwrite(remap = false)
	public static Executor method_27958() {
		if (!SmoothBootState.initIOWorker) {
			field_24477 = replIoWorker();
			SmoothBoot.LOGGER.debug("IO worker replaced");
			SmoothBootState.initIOWorker = true;
		}
		return field_24477;
	}
	
	// Replace createWorker
	private static ExecutorService replWorker(String name) {
		if (!SmoothBootState.initConfig) {
			SmoothBoot.regConfig();
			SmoothBootState.initConfig = true;
		}
		
		ExecutorService executorService2 = new ForkJoinPool(MathHelper.clamp(select(name, SmoothBoot.config.threadCount.bootstrap,
			SmoothBoot.config.threadCount.main), 1, 0x7fff), (forkJoinPool) -> {
				String workerName = "Worker-" + name + "-" + NEXT_SERVER_WORKER_ID.getAndIncrement();
				SmoothBoot.LOGGER.debug("Initialized " + workerName);
				
				ForkJoinWorkerThread forkJoinWorkerThread = new LoggingForkJoinWorkerThread(forkJoinPool, LOGGER);
				forkJoinWorkerThread.setPriority(select(name, SmoothBoot.config.threadPriority.bootstrap,
					SmoothBoot.config.threadPriority.main));
				forkJoinWorkerThread.setName(workerName);
				return forkJoinWorkerThread;
		}, UtilMixin::method_18347, true);
		return executorService2;
	}
	
	// Replace createIoWorker
	private static ExecutorService replIoWorker() {
		return Executors.newCachedThreadPool((runnable) -> {
			String workerName = "IO-Worker-" + NEXT_SERVER_WORKER_ID.getAndIncrement();
			SmoothBoot.LOGGER.debug("Initialized " + workerName);
			
			Thread thread = new Thread(runnable);
			thread.setName(workerName);
			thread.setDaemon(true);
			thread.setPriority(SmoothBoot.config.threadPriority.io);
			thread.setUncaughtExceptionHandler(UtilMixin::method_18347);
			return thread;
		});
	}
	
	private static <T> T select(String name, T bootstrap, T main) {
		return Objects.equals(name, "Bootstrap") ? bootstrap : main;
	}
}
