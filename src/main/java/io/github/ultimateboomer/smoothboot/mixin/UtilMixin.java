package io.github.ultimateboomer.smoothboot.mixin;

import io.github.ultimateboomer.smoothboot.SmoothBoot;
import io.github.ultimateboomer.smoothboot.util.LoggingForkJoinWorkerThread;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(Util.class)
public abstract class UtilMixin {
	@Shadow @Final @Mutable
	private static ExecutorService BOOTSTRAP_EXECUTOR;
	
	@Shadow @Final @Mutable
	private static ExecutorService MAIN_WORKER_EXECUTOR;
	
	@Shadow @Final @Mutable
	private static ExecutorService IO_WORKER_EXECUTOR;
	
	@Shadow @Final
	private static AtomicInteger NEXT_WORKER_ID;
	
	@Shadow
	private static void uncaughtExceptionHandler(Thread thread, Throwable throwable) {}

	@Inject(method = "getBootstrapExecutor", at = @At("HEAD"))
	private static void onGetBootstrapExecutor(CallbackInfoReturnable<Executor> ci) {
		if (!SmoothBoot.initBootstrap) {
			BOOTSTRAP_EXECUTOR = replWorker("Bootstrap");
			SmoothBoot.LOGGER.debug("Bootstrap worker replaced");
			SmoothBoot.initBootstrap = true;
		}
	}

	@Inject(method = "getMainWorkerExecutor", at = @At("HEAD"))
	private static void onGetMainWorkerExecutor(CallbackInfoReturnable<Executor> ci) {
		if (!SmoothBoot.initMainWorker) {
			MAIN_WORKER_EXECUTOR = replWorker("Main");
			SmoothBoot.LOGGER.debug("Main worker replaced");
			SmoothBoot.initMainWorker = true;
		}
	}

	@Inject(method = "getIoWorkerExecutor", at = @At("HEAD"))
	private static void onGetIoWorkerExecutor(CallbackInfoReturnable<Executor> ci) {
		if (!SmoothBoot.initIOWorker) {
			IO_WORKER_EXECUTOR = replIoWorker();
			SmoothBoot.LOGGER.debug("IO worker replaced");
			SmoothBoot.initIOWorker = true;
		}
	}

	/**
	 * Replace
	 */
	private static ExecutorService replWorker(String name) {
		if (!SmoothBoot.initConfig) {
			SmoothBoot.regConfig();
			SmoothBoot.initConfig = true;
		}

		return new ForkJoinPool(MathHelper.clamp(select(name, SmoothBoot.config.threadCount.bootstrap,
			SmoothBoot.config.threadCount.main), 1, 0x7fff), (forkJoinPool) -> {
				String workerName = "Worker-" + name + "-" + NEXT_WORKER_ID.getAndIncrement();
				SmoothBoot.LOGGER.debug("Initialized " + workerName);

				ForkJoinWorkerThread forkJoinWorkerThread = new LoggingForkJoinWorkerThread(forkJoinPool, SmoothBoot.LOGGER);
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
