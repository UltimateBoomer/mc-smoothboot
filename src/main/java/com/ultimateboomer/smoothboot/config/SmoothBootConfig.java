package com.ultimateboomer.smoothboot.config;

public class SmoothBootConfig {
	private int bootstrapThreads;
	private int mainThreads;
	private int bootstrapPriority;
	private int mainPriority;
	private int ioPriority;
	private int modLoadingPriority;

	public int getBootstrapThreads() {
		return bootstrapThreads;
	}

	public void setBootstrapThreads(int bootstrapThreads) {
		this.bootstrapThreads = bootstrapThreads;
	}

	public int getMainThreads() {
		return mainThreads;
	}

	public void setMainThreads(int mainThreads) {
		this.mainThreads = mainThreads;
	}

	public int getBootstrapPriority() {
		return bootstrapPriority;
	}

	public void setBootstrapPriority(int bootstrapPriority) {
		this.bootstrapPriority = bootstrapPriority;
	}

	public int getMainPriority() {
		return mainPriority;
	}

	public void setMainPriority(int mainPriority) {
		this.mainPriority = mainPriority;
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
