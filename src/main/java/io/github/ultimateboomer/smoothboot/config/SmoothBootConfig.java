package io.github.ultimateboomer.smoothboot.config;

import net.minecraft.util.math.MathHelper;

public class SmoothBootConfig {
	private int serverThreads = Math.min(Runtime.getRuntime().availableProcessors() - 1, 7);

	private int gamePriority = 5;
	private int integratedServerPriority;
	private int mainPriority = 1;
	private int modLoadingPriority = 1;

	private boolean optimizeDataFixerBuild = true;
	
	/**
	 * Make sure the config values are within range
	 */
	public void validate() {
		serverThreads = Math.max(serverThreads, 1);

		gamePriority = MathHelper.clamp(gamePriority, 1, 10);
		integratedServerPriority = MathHelper.clamp(integratedServerPriority, 1, 10);
		mainPriority = MathHelper.clamp(mainPriority, 1, 10);
		modLoadingPriority = MathHelper.clamp(modLoadingPriority, 1, 10);
	}

	public int getServerThreads() {
		return serverThreads;
	}

	public void setServerThreads(int mainThreads) {
		this.serverThreads = mainThreads;
	}

	public int getGamePriority() {
		return gamePriority;
	}

	public void setGamePriority(int gamePriority) {
		this.gamePriority = gamePriority;
	}

	public int getIntegratedServerPriority() {
		return integratedServerPriority;
	}

	public void setIntegratedServerPriority(int integratedServerPriority) {
		this.integratedServerPriority = integratedServerPriority;
	}

	public int getMainPriority() {
		return mainPriority;
	}

	public void setMainPriority(int mainPriority) {
		this.mainPriority = mainPriority;
	}

	public int getModLoadingPriority() {
		return modLoadingPriority;
	}

	public void setModLoadingPriority(int modLoadingPriority) {
		this.modLoadingPriority = modLoadingPriority;
	}

	public boolean isOptimizeDataFixerBuild() {
		return optimizeDataFixerBuild;
	}

	public void setOptimizeDataFixerBuild(boolean optimizeDataFixerBuild) {
		this.optimizeDataFixerBuild = optimizeDataFixerBuild;
	}
}
