package io.github.ultimateboomer.smoothboot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SmoothBootState {
	public static boolean initConfig = false;
	public static boolean initServerWorker = false;
	public static boolean initIOWorker = false;
	public static boolean gameLoaded = false;

	public static final ExecutorService SINGLE_THREADED_EXECUTOR = Executors.newSingleThreadExecutor();

}
