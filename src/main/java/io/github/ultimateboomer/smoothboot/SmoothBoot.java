package io.github.ultimateboomer.smoothboot;

import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SmoothBoot.MOD_ID)
public class SmoothBoot {
	public static final String MOD_ID = "smoothboot";
	public static final String MOD_NAME = "Smooth Boot";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

	public SmoothBoot() {
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
				() -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
	}
}
