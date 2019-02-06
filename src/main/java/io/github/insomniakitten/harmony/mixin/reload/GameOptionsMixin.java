package io.github.insomniakitten.harmony.mixin.reload;

import io.github.insomniakitten.harmony.reload.AssetReloader;
import io.github.insomniakitten.harmony.reload.VanillaAssetGroup;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.CompletableFuture;

@Mixin(GameOptions.class)
abstract class GameOptionsMixin {
  private GameOptionsMixin() {}

  @Redirect(
    method = "method_1625",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/MinecraftClient;reloadResourcesConcurrently()Ljava/util/concurrent/CompletableFuture;"
    )
  )
  private CompletableFuture<Object> method_1625ReloadResources(final MinecraftClient client) {
    return AssetReloader.scheduleReload(client, VanillaAssetGroup.LANGUAGES);
  }
}
