package io.github.ultimateboomer.smoothboot.mixin;

import com.google.common.base.Objects;
import io.github.ultimateboomer.smoothboot.SmoothBoot;
import io.github.ultimateboomer.smoothboot.config.SmoothBootConfig;
import io.github.ultimateboomer.smoothboot.config.SmoothBootConfigHandler;
import io.github.ultimateboomer.smoothboot.util.LoggingForkJoinWorkerThread;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(Util.class)
public abstract class UtilMixin {
	private static boolean initBootstrapExecutor = false;
	private static boolean initMainWorkerExecutor = false;
	private static boolean initIoWorker = false;

	@Shadow
	private static ExecutorService BOOTSTRAP_SERVICE;

	@Shadow
	private static ExecutorService SERVER_EXECUTOR;

	@Shadow
	private static ExecutorService RENDERING_SERVICE;
	
	@Shadow
	@Final
	private static AtomicInteger NEXT_SERVER_WORKER_ID;
	
	@Shadow
	@Final
	private static Logger LOGGER;
	
	@Shadow
	protected static void printException(Thread thread, Throwable throwable) {};
	
	// Probably not ideal, but this is the only way I found to modify createWorker without causing errors.
	// Redirecting or overwriting causes static initialization to be called too early resulting in NullPointerException being thrown.
	@Overwrite
	public static Executor getBootstrapService() {
		if (!initBootstrapExecutor) {
			BOOTSTRAP_SERVICE = replWorker("Bootstrap");
			initBootstrapExecutor = true;
			SmoothBoot.LOGGER.debug("Replaced Bootstrap Executor");
		}

		return BOOTSTRAP_SERVICE;
	}

	@Overwrite
	public static Executor getServerExecutor() {
		if (!initMainWorkerExecutor) {
			SERVER_EXECUTOR = replWorker("Main");
			initMainWorkerExecutor = true;
			SmoothBoot.LOGGER.debug("Replaced Main Executor");
		}

		return SERVER_EXECUTOR;
	}

	@Overwrite
	public static Executor getRenderingService() {
		if (!initIoWorker) {
			RENDERING_SERVICE = replIoWorker();
			initIoWorker = true;
			SmoothBoot.LOGGER.debug("Replaced IO Executor");
		}
		return RENDERING_SERVICE;
	}
	
	// Replace createNamedService
	private static ExecutorService replWorker(String name) {
		if (SmoothBootConfigHandler.config == null) {
			try {
				SmoothBootConfigHandler.readConfig();
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}

		SmoothBootConfig config = SmoothBootConfigHandler.config;
		ExecutorService executorService2 = new ForkJoinPool(MathHelper.clamp(select(name, config.getBootstrapThreads(),
			config.getMainThreads()), 1, 0x7fff), (forkJoinPool) -> {
			String workerName = "Worker-" + name + NEXT_SERVER_WORKER_ID.getAndIncrement();
			SmoothBoot.LOGGER.debug("Initialized " + workerName);
			
			ForkJoinWorkerThread forkJoinWorkerThread = new LoggingForkJoinWorkerThread(forkJoinPool, LOGGER);
			forkJoinWorkerThread.setPriority(select(name, config.getBootstrapPriority(), config.getMainPriority()));
			forkJoinWorkerThread.setName(workerName);
			return forkJoinWorkerThread;
		}, UtilMixin::printException, true);
		return executorService2;
	}
	
	private static ExecutorService replIoWorker() {
		return Executors.newCachedThreadPool((p_240978_0_) -> {
			String workerName = "IO-Worker-" + NEXT_SERVER_WORKER_ID.getAndIncrement();
			SmoothBoot.LOGGER.debug("Initialized " + workerName);
			
			Thread thread = new Thread(p_240978_0_);
			thread.setName(workerName);
			thread.setPriority(SmoothBootConfigHandler.config.getIoPriority());
			thread.setUncaughtExceptionHandler(UtilMixin::printException);
			return thread;
		});
	}
	
	private static <T> T select(String name, T bootstrap, T main) {
		return Objects.equal(name, "Bootstrap") ? bootstrap : main;
	}
}
