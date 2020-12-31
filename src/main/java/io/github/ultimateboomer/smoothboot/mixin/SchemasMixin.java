package io.github.ultimateboomer.smoothboot.mixin;

import io.github.ultimateboomer.smoothboot.SmoothBoot;
import io.github.ultimateboomer.smoothboot.SmoothBootState;
import net.minecraft.datafixer.Schemas;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.Executor;

@Mixin(Schemas.class)
public class SchemasMixin {
    // This method scales terribly with more threads, so it's better to run it with the current thread
    @Redirect(method = "create", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/Util;getServerWorkerExecutor()Ljava/util/concurrent/Executor;"))
    private static Executor onCreate() {
        if (SmoothBoot.config.optimizations.dataFixers) {
            SmoothBoot.LOGGER.debug("Schemas.create called, executor replaced");
            return SmoothBootState.SINGLE_THREADED_EXECUTOR;
        } else {
            return Util.getServerWorkerExecutor();
        }
    }
}
