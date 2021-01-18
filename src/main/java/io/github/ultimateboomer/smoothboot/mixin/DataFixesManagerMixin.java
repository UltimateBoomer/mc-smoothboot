package io.github.ultimateboomer.smoothboot.mixin;

import io.github.ultimateboomer.smoothboot.SmoothBoot;
import io.github.ultimateboomer.smoothboot.SmoothBootState;
import io.github.ultimateboomer.smoothboot.config.SmoothBootConfigHandler;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.DataFixesManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.util.concurrent.Executor;

@Mixin(DataFixesManager.class)
public class DataFixesManagerMixin {
    // This method scales terribly with more threads, so it's better to run it with the current thread
    @Redirect(method = "createFixer", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/Util;getServerExecutor()Ljava/util/concurrent/Executor;"))
    private static Executor onCreate() {
        if (!SmoothBootState.initConfig) {
            try {
                SmoothBootConfigHandler.readConfig();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        if (SmoothBootConfigHandler.config.isOptimizeDataFixerBuild()) {
            SmoothBoot.LOGGER.debug("DataFixesManager.createFixer called, executor replaced");
            return SmoothBootState.SINGLE_THREADED_EXECUTOR;
        } else {
            return Util.getServerExecutor();
        }
    }
}
