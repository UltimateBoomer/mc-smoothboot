package io.github.ultimateboomer.smoothboot.mixin;

import io.github.ultimateboomer.smoothboot.SmoothBootState;
import net.minecraftforge.fml.DatagenModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;

@Mixin(DatagenModLoader.class)
public class DatagenModLoaderMixin {
	@Shadow
	private static boolean runningDataGen;

	@Inject(method = "begin(Ljava/util/Set;Ljava/nio/file/Path;Ljava/util/Collection;Ljava/util/Collection;ZZZZZZ)V", at = @At(value = "INVOKE", target = "net/minecraft/util/registry/Bootstrap.register()V"), remap = false)
	private static void begin(final Set<String> mods, final Path path, final java.util.Collection<Path> inputs,
							  Collection<Path> existingPacks, final boolean serverGenerators,
							  final boolean clientGenerators, final boolean devToolGenerators,
							  final boolean reportsGenerator, final boolean structureValidator, final boolean flat,
							  CallbackInfo ci) {
		if (runningDataGen) {
			SmoothBootState.mcIsRunningDatagen = true;
		}
	}
}
