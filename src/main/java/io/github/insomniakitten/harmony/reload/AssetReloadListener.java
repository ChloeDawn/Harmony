package io.github.insomniakitten.harmony.reload;

import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloadListener;

import java.util.function.Predicate;

public interface AssetReloadListener extends ResourceReloadListener {
  void onResourceReload(final ResourceManager resourceManager, final Predicate<AssetGroup> predicate);

  @Override
  default void onResourceReload(final ResourceManager resourceManager) {
    this.onResourceReload(resourceManager, group -> true);
  }
}
