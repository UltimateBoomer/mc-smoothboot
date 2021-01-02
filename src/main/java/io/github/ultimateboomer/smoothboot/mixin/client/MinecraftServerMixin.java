package io.github.ultimateboomer.smoothboot.mixin.client;

import io.github.ultimateboomer.smoothboot.SmoothBoot;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Shadow
    @Final
    protected Thread serverThread;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void onInit(CallbackInfo ci) {
        serverThread.setPriority(SmoothBoot.config.threadPriority.integratedServer);
        SmoothBoot.LOGGER.debug("Initialized integrated server thread");
    }
}
