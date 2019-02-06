package io.github.insomniakitten.harmony.mixin.reload;

import io.github.insomniakitten.harmony.reload.AssetReloader;
import io.github.insomniakitten.harmony.reload.VanillaAssetGroup;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.client.gui.menu.settings.LanguageSettingsScreen$class_427")
abstract class LanguageSettingsScreen$class_247Mixin {
  private LanguageSettingsScreen$class_247Mixin() {}

  @Redirect(
    method = "selectEntry",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/MinecraftClient;reloadResources()V"
    )
  )
  private void method_1937ReloadResources(final MinecraftClient client) {
    AssetReloader.reload(client, VanillaAssetGroup.LANGUAGES);
  }
}
