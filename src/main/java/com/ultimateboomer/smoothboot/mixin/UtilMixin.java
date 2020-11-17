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
import com.ultimateboomer.smoothboot.SmoothBootState;
import com.ultimateboomer.smoothboot.util.LoggingForkJoinWorkerThread;

import net.minecraft.Bootstrap;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.math.MathHelper;

@Mixin(Util.class)
public abstract class UtilMixin {
	@Shadow
	private static ExecutorService SERVER_WORKER_EXECUTOR;
	
	@Shadow
	@Final
	private static AtomicInteger NEXT_SERVER_WORKER_ID;
	
	@Shadow
	@Final
	private static Logger LOGGER;
	
	@Shadow
	protected static <T extends Throwable> T throwOrPause(T t) { return null; };
	
	// Probably not ideal, but this is the only way I found to modify createWorker without causing errors.
	// Redirecting or overwriting causes static initialization to be called too early resulting in NullPointerException being thrown.
	@Overwrite()
	public static Executor getServerWorkerExecutor() {
		if (!SmoothBootState.initServerWorker) {
			SERVER_WORKER_EXECUTOR = replWorker("Main");
			SmoothBoot.LOGGER.debug("Main worker replaced");
			SmoothBootState.initServerWorker = true;
		}
		
		return SERVER_WORKER_EXECUTOR;
	}
	
	// Replace createServerWorkerExecutor
	private static ExecutorService replWorker(String name) {
		if (!SmoothBootState.initConfig) {
			SmoothBoot.regConfig();
			SmoothBootState.initConfig = true;
		}
		
		ExecutorService executorService2 = new ForkJoinPool(MathHelper.clamp(SmoothBoot.config.serverThreadCount,
			1, 0x7fff), (forkJoinPool) -> {
				String workerName = "Server-Worker-" + name + "-" + NEXT_SERVER_WORKER_ID.getAndIncrement();
				SmoothBoot.LOGGER.debug("Initialized " + workerName);
				
				ForkJoinWorkerThread forkJoinWorkerThread = new LoggingForkJoinWorkerThread(forkJoinPool, LOGGER);
				forkJoinWorkerThread.setPriority(SmoothBoot.config.serverPriority);
				forkJoinWorkerThread.setName(workerName);
				return forkJoinWorkerThread;
		}, (thread, throwable) -> {
            throwOrPause(throwable);
            if (throwable instanceof CompletionException) {
               throwable = throwable.getCause();
            }
            
            if (throwable instanceof CrashException) {
               Bootstrap.println(((CrashException)throwable).getReport().asString());
               System.exit(-1);
            }
            
            LOGGER.error(String.format("Caught exception in thread %s", thread), throwable);
         }, true);
		return executorService2;
	}
}
