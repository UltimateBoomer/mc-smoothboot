package io.github.ultimateboomer.smoothboot.mixin.client;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import io.github.ultimateboomer.smoothboot.SmoothBoot;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.UserCache;
import net.minecraft.util.registry.RegistryTracker;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void onInit(Thread thread, MinecraftClient minecraftClient, RegistryTracker.Modifiable modifiable,
                       LevelStorage.Session session, ResourcePackManager<ResourcePackProfile> resourcePackManager,
                       ServerResourceManager serverResourceManager, SaveProperties saveProperties,
                       MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository,
                       UserCache userCache, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory,
                       CallbackInfo ci) {
        thread.setPriority(SmoothBoot.config.threadPriority.integratedServer);
        SmoothBoot.LOGGER.debug("Initialized integrated server thread");
    }
}
