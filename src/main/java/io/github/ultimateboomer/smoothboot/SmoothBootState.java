package io.github.ultimateboomer.smoothboot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SmoothBootState {
	public static boolean initConfig = false;
	public static boolean initServerWorker = false;

	public static final ExecutorService SINGLE_THREADED_EXECUTOR = Executors.newSingleThreadExecutor(r -> {
		Thread thread = new Thread(r, "1T Executor Thread");
		thread.setPriority(SmoothBoot.config.threadPriority.main);
		return null;
	});

}
