package com.ultimateboomer.smoothboot.mixin;

import java.util.concurrent.CompletionException;
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
import com.ultimateboomer.smoothboot.config.SmoothBootConfig;
import com.ultimateboomer.smoothboot.config.SmoothBootConfigHandler;
import com.ultimateboomer.smoothboot.util.LoggingForkJoinWorkerThread;

import net.minecraft.crash.ReportedException;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Bootstrap;

@Mixin(Util.class)
public abstract class UtilMixin {
	private static boolean initConfig = false;
	private static boolean initServerExecutor = false;
	
	@Shadow
	private static ExecutorService SERVER_EXECUTOR;
	
	@Shadow
	@Final
	private static AtomicInteger NEXT_SERVER_WORKER_ID;
	
	@Shadow
	@Final
	private static Logger LOGGER;
	
	// Probably not ideal, but this is the only way I found to modify createWorker without causing errors.
	// Redirecting or overwriting causes static initialization to be called too early resulting in NullPointerException being thrown.
	@Overwrite()
	public static Executor getServerExecutor() {
		if (!initServerExecutor) {
			SERVER_EXECUTOR = replWorker("Main");
			initServerExecutor = true;
		}
		return SERVER_EXECUTOR;
	}
	
	// Replace createServerExecutor
	private static ExecutorService replWorker(String name) {
		if (!initConfig) {
			SmoothBootConfigHandler.readConfig();
			initConfig = true;
		}
		SmoothBootConfig config = SmoothBootConfigHandler.config;
		
		ForkJoinPool executorService2 = new ForkJoinPool(MathHelper.clamp(config.getMainThreads(),
			1, 0x7fff), (forkJoinPool) -> {
			String workerName = "Server-Worker-" + name + NEXT_SERVER_WORKER_ID.getAndIncrement();
			SmoothBoot.LOGGER.info("Initialized " + workerName);
			
			ForkJoinWorkerThread forkJoinWorkerThread = new LoggingForkJoinWorkerThread(forkJoinPool, LOGGER);
			forkJoinWorkerThread.setPriority(config.getMainPriority());
			forkJoinWorkerThread.setName(workerName);
			return forkJoinWorkerThread;
		}, (thread, throwable) -> {
            if (throwable instanceof CompletionException) {
                throwable = throwable.getCause();
             }
             
             if (throwable instanceof ReportedException) {
                Bootstrap.printToSYSOUT(((ReportedException)throwable).getCrashReport().getCompleteReport());
                System.exit(-1);
             }
             
             LOGGER.error(String.format("Caught exception in thread %s", thread), throwable);
          }, true);
		SmoothBoot.LOGGER.info(executorService2);
		return (ExecutorService) executorService2;
	}
}
