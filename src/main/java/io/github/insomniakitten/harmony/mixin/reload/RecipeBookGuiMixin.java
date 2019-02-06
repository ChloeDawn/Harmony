package io.github.insomniakitten.harmony.mixin.reload;

import io.github.insomniakitten.harmony.reload.AssetReloader;
import io.github.insomniakitten.harmony.reload.VanillaAssetGroup;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RecipeBookGui.class)
abstract class RecipeBookGuiMixin {
  private RecipeBookGuiMixin() {}

  @Redirect(
    method = "triggerPirateSpeakEasterEgg",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/MinecraftClient;reloadResources()V"
    )
  )
  private void method_1937ReloadResources(final MinecraftClient client) {
    AssetReloader.reload(client, VanillaAssetGroup.LANGUAGES);
  }
}
