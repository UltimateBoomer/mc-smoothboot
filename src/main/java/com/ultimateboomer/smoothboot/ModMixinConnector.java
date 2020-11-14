package com.ultimateboomer.smoothboot;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class ModMixinConnector implements IMixinConnector {
	@Override
	public void connect() {
		Mixins.addConfiguration("smoothboot.mixins.json");
		SmoothBoot.LOGGER.info("Mixin Connected");
	}
}
