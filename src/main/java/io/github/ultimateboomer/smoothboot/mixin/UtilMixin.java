package io.github.ultimateboomer.smoothboot.mixin;

import io.github.ultimateboomer.smoothboot.SmoothBoot;
import io.github.ultimateboomer.smoothboot.SmoothBootState;
import io.github.ultimateboomer.smoothboot.util.LoggingForkJoinWorkerThread;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.*;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(Util.class)
public abstract class UtilMixin {
	@Mutable
	@Final
	@Shadow
	private static ExecutorService BOOTSTRAP_EXECUTOR;
	
	@Mutable
	@Final
	@Shadow
	private static ExecutorService MAIN_WORKER_EXECUTOR;
	
	@Mutable
	@Final
	@Shadow
	private static ExecutorService IO_WORKER_EXECUTOR;
	
	@Shadow
	@Final
	private static AtomicInteger NEXT_WORKER_ID;
	
	@Shadow
	@Final
	static Logger LOGGER;
	
	@Shadow
	private static void uncaughtExceptionHandler(Thread thread, Throwable throwable) {};
	
	// Probably not ideal, but this is the only way I found to modify createWorker without causing errors.
	// Redirecting or overwriting causes static initialization to be called too early resulting in NullPointerException being thrown.


	/**
	 * @author UltimateBoomer
	 */
	@Overwrite
	public static ExecutorService getBootstrapExecutor() {
		if (!SmoothBootState.initBootstrap) {
			BOOTSTRAP_EXECUTOR = replWorker("Bootstrap");
			SmoothBoot.LOGGER.debug("Bootstrap worker replaced");
			SmoothBootState.initBootstrap = true;
		}
		return BOOTSTRAP_EXECUTOR;
	}
	
	/**
	 * @author UltimateBoomer
	 */
	@Overwrite
	public static ExecutorService getMainWorkerExecutor() {
		if (!SmoothBootState.initMainWorker) {
			MAIN_WORKER_EXECUTOR = replWorker("Main");
			SmoothBoot.LOGGER.debug("Main worker replaced");
			SmoothBootState.initMainWorker = true;
		}
		return MAIN_WORKER_EXECUTOR;
	}
	
	/**
	 * @author UltimateBoomer
	 */
	@Overwrite
	public static ExecutorService getIoWorkerExecutor() {
		if (!SmoothBootState.initIOWorker) {
			IO_WORKER_EXECUTOR = replIoWorker();
			SmoothBoot.LOGGER.debug("IO worker replaced");
			SmoothBootState.initIOWorker = true;
		}
		return IO_WORKER_EXECUTOR;
	}

	/**
	 * Replace
	 */
	private static ExecutorService replWorker(String name) {
		if (!SmoothBootState.initConfig) {
			SmoothBoot.regConfig();
			SmoothBootState.initConfig = true;
		}

		return new ForkJoinPool(MathHelper.clamp(select(name, SmoothBoot.config.threadCount.bootstrap,
			SmoothBoot.config.threadCount.main), 1, 0x7fff), (forkJoinPool) -> {
				String workerName = "Worker-" + name + "-" + NEXT_WORKER_ID.getAndIncrement();
				SmoothBoot.LOGGER.debug("Initialized " + workerName);

				ForkJoinWorkerThread forkJoinWorkerThread = new LoggingForkJoinWorkerThread(forkJoinPool, LOGGER);
				forkJoinWorkerThread.setPriority(select(name, SmoothBoot.config.threadPriority.bootstrap,
					SmoothBoot.config.threadPriority.main));
				forkJoinWorkerThread.setName(workerName);
				return forkJoinWorkerThread;
		}, UtilMixin::uncaughtExceptionHandler, true);
	}

	/**
	 * Replace
	 */
	private static ExecutorService replIoWorker() {
		return Executors.newCachedThreadPool((runnable) -> {
			String workerName = "IO-Worker-" + NEXT_WORKER_ID.getAndIncrement();
			SmoothBoot.LOGGER.debug("Initialized " + workerName);
			
			Thread thread = new Thread(runnable);
			thread.setName(workerName);
			thread.setDaemon(true);
			thread.setPriority(SmoothBoot.config.threadPriority.io);
			thread.setUncaughtExceptionHandler(UtilMixin::uncaughtExceptionHandler);
			return thread;
		});
	}
	
	private static <T> T select(String name, T bootstrap, T main) {
		return Objects.equals(name, "Bootstrap") ? bootstrap : main;
	}
}
