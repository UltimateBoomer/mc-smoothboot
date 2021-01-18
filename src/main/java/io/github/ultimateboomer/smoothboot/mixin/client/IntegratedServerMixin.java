package io.github.ultimateboomer.smoothboot.mixin.client;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import io.github.ultimateboomer.smoothboot.SmoothBoot;
import io.github.ultimateboomer.smoothboot.SmoothBootState;
import io.github.ultimateboomer.smoothboot.config.SmoothBootConfig;
import io.github.ultimateboomer.smoothboot.config.SmoothBootConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.DataPackRegistries;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.chunk.listener.IChunkStatusListenerFactory;
import net.minecraft.world.storage.IServerConfiguration;
import net.minecraft.world.storage.SaveFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {
	@Inject(method = "<init>", at = @At("RETURN"))
	public void onInit(Thread p_i232494_1_, Minecraft p_i232494_2_, DynamicRegistries.Impl p_i232494_3_,
					   SaveFormat.LevelSave p_i232494_4_, ResourcePackList p_i232494_5_,
					   DataPackRegistries p_i232494_6_, IServerConfiguration p_i232494_7_,
					   MinecraftSessionService p_i232494_8_, GameProfileRepository p_i232494_9_,
					   PlayerProfileCache p_i232494_10_, IChunkStatusListenerFactory p_i232494_11_,
					   CallbackInfo ci) {
		if (!SmoothBootState.mcIsRunningDatagen) {
			p_i232494_1_.setPriority(SmoothBootConfigHandler.config.getIntegratedServerPriority());
			SmoothBoot.LOGGER.debug("Initialized integrated server thread");
		}
	}
}
