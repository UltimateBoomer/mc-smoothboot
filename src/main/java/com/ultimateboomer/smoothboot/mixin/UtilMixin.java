package com.ultimateboomer.smoothboot.mixin;

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

import com.google.common.base.Objects;
import com.ultimateboomer.smoothboot.SmoothBoot;
import com.ultimateboomer.smoothboot.config.SmoothBootConfig;
import com.ultimateboomer.smoothboot.config.SmoothBootConfigHandler;
import com.ultimateboomer.smoothboot.util.LoggingForkJoinWorkerThread;

import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

@Mixin(Util.class)
public abstract class UtilMixin {
	private static boolean initConfig = false;
	private static boolean initBootstrapExecutor = false;
	private static boolean initMainWorkerExecutor = false;
	private static boolean initIoWorker = false;
	
	@Shadow
	private static ExecutorService field_240974_d_;
	
	@Shadow
	private static ExecutorService SERVER_EXECUTOR;
	
	@Shadow
	private static ExecutorService field_240975_f_;
	
	@Shadow
	@Final
	private static AtomicInteger NEXT_SERVER_WORKER_ID;
	
	@Shadow
	@Final
	private static Logger LOGGER;
	
	@Shadow
	protected static void func_240983_a_(Thread thread, Throwable throwable) {};
	
	// Probably not ideal, but this is the only way I found to modify createWorker without causing errors.
	// Redirecting or overwriting causes static initialization to be called too early resulting in NullPointerException being thrown.
	@Overwrite()
	public static Executor func_240991_e_() {
		if (!initBootstrapExecutor) {
			field_240974_d_ = replWorker("Bootstrap");
			initBootstrapExecutor = true;
		}
		return field_240974_d_;
	}
	
	@Overwrite()
	public static Executor getServerExecutor() {
		if (!initMainWorkerExecutor) {
			SERVER_EXECUTOR = replWorker("Main");
			initMainWorkerExecutor = true;
		}
		return SERVER_EXECUTOR;
	}
	
	@Overwrite
	public static Executor func_240992_g_() {
		if (!initIoWorker) {
			field_240975_f_ = replIoWorker();
			initIoWorker = true;
		}
		return field_240975_f_;
	}
	
	// Replace createNamedService
	private static ExecutorService replWorker(String name) {
		if (!initConfig) {
			SmoothBootConfigHandler.readConfig();
			initConfig = true;
		}
		SmoothBootConfig config = SmoothBootConfigHandler.config;
		Object executorService2 = new ForkJoinPool(MathHelper.clamp(select(name, config.getBootstrapThreads(),
			config.getMainThreads()), 1, 0x7fff), (forkJoinPool) -> {
			String workerName = "Worker-" + name + NEXT_SERVER_WORKER_ID.getAndIncrement();
			SmoothBoot.LOGGER.debug("Initialized " + workerName);
			
			ForkJoinWorkerThread forkJoinWorkerThread = new LoggingForkJoinWorkerThread(forkJoinPool, LOGGER);
			forkJoinWorkerThread.setPriority(select(name, config.getBootstrapPriority(), config.getMainPriority()));
			forkJoinWorkerThread.setName(workerName);
			return forkJoinWorkerThread;
		}, UtilMixin::func_240983_a_, true);
		return (ExecutorService) executorService2;
	}
	
	private static ExecutorService replIoWorker() {
		return Executors.newCachedThreadPool((p_240978_0_) -> {
			String workerName = "IO-Worker-" + NEXT_SERVER_WORKER_ID.getAndIncrement();
			SmoothBoot.LOGGER.debug("Initialized " + workerName);
			
			Thread thread = new Thread(p_240978_0_);
			thread.setName(workerName);
			thread.setPriority(SmoothBootConfigHandler.config.getIoPriority());
			thread.setUncaughtExceptionHandler(UtilMixin::func_240983_a_);
			return thread;
		});
	}
	
	private static <T> T select(String name, T bootstrap, T main) {
		return Objects.equal(name, "Bootstrap") ? bootstrap : main;
	}
}
