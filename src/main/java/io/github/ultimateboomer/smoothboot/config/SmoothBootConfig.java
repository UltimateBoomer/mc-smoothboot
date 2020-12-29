package io.github.ultimateboomer.smoothboot.config;

import net.minecraft.util.math.MathHelper;

public class SmoothBootConfig {
	private int serverThreads = Math.min(Runtime.getRuntime().availableProcessors() - 1, 7);
	private int gamePriority = 5;
	private int serverPriority = 1;
	private int ioPriority = 5;
	private int modLoadingPriority = 1;
	
	/**
	 * Make sure the config values are within range
	 */
	public void validate() {
		serverThreads = Math.max(serverThreads, 1);
		gamePriority = MathHelper.clamp(gamePriority, 1, 10);
		serverPriority = MathHelper.clamp(serverPriority, 1, 10);
		ioPriority = MathHelper.clamp(ioPriority, 1, 10);
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

	public int getServerPriority() {
		return serverPriority;
	}

	public void setServerPriority(int mainPriority) {
		this.serverPriority = mainPriority;
	}

	public int getIoPriority() {
		return ioPriority;
	}

	public void setIoPriority(int ioPriority) {
		this.ioPriority = ioPriority;
	}

	public int getModLoadingPriority() {
		return modLoadingPriority;
	}

	public void setModLoadingPriority(int modLoadingPriority) {
		this.modLoadingPriority = modLoadingPriority;
	}
}
