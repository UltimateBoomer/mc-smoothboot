package io.github.ultimateboomer.smoothboot.config;

import io.github.ultimateboomer.smoothboot.SmoothBoot;
import net.minecraft.util.math.MathHelper;

public class SmoothBootConfig {
	public ThreadCount threadCount = new ThreadCount();

	public ThreadPriority threadPriority = new ThreadPriority();
	
	public static class ThreadCount {
		public int bootstrap = 1;
		public int main = MathHelper.clamp(Runtime.getRuntime().availableProcessors() - 1, 1,
				SmoothBoot.getMaxBackgroundThreads());
	}
	
	public static class ThreadPriority {
		public int game = 5;
		public int bootstrap = 1;
		public int main = 1;
		public int io = 1;
		public int integratedServer = 5;
	}

	public void validate() {
		threadCount.bootstrap = Math.max(threadCount.bootstrap, 1);
		threadCount.main = Math.max(threadCount.main, 1);

		threadPriority.game = MathHelper.clamp(threadPriority.game, 1, 10);
		threadPriority.integratedServer = MathHelper.clamp(threadPriority.integratedServer, 1, 10);
		threadPriority.bootstrap = MathHelper.clamp(threadPriority.bootstrap, 1, 10);
		threadPriority.main = MathHelper.clamp(threadPriority.main, 1, 10);
		threadPriority.io = MathHelper.clamp(threadPriority.io, 1, 10);
	}
}
 