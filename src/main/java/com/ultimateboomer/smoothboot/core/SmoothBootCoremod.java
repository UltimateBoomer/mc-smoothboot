package com.ultimateboomer.smoothboot.core;

import java.util.Map;

import javax.annotation.Nullable;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import com.ultimateboomer.smoothboot.SmoothBoot;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class SmoothBootCoremod implements IFMLLoadingPlugin {
	
	public SmoothBootCoremod() {
		MixinBootstrap.init();
		Mixins.addConfiguration("smoothboot.mixins.json");
		SmoothBoot.LOGGER.info("Successfully loaded mod mixin");
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[0];
	}

	@Override
	public String getModContainerClass() {
		return null;
	}
	
	@Nullable
	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
