package io.github.insomniakitten.harmony.reload;

import com.google.common.base.MoreObjects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.audio.SoundLoader;
import net.minecraft.client.font.FontRendererManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.resource.FoliageColormapResourceLoader;
import net.minecraft.client.resource.GrassColormapResourceLoader;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.client.search.SearchManager;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourceReloadListener;

import java.util.ConcurrentModificationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public final class AssetReloader {
  private AssetReloader() {}

  public static void reload(final MinecraftClient client, final AssetGroup group) {
    ReloadPredicate.run(client::reloadResources, group::equals);
  }

  public static CompletableFuture<Object> scheduleReload(final MinecraftClient client, final AssetGroup group) {
    return client.executeFuture(() -> reload(client, group));
  }

  public static boolean canReload(final ResourceReloadListener listener) {
    final Class<? extends ResourceReloadListener> type = listener.getClass();
    final Predicate<AssetGroup> predicate = ReloadPredicate.currentOrDefault();
    if (LanguageManager.class == type) return predicate.test(VanillaAssetGroup.LANGUAGES);
    if (SearchManager.class == type) return predicate.test(VanillaAssetGroup.LANGUAGES);
    if (TextureManager.class == type) return predicate.test(VanillaAssetGroup.TEXTURES);
    if (FontRendererManager.class == type) return predicate.test(VanillaAssetGroup.TEXTURES);
    if (FoliageColormapResourceLoader.class == type) return predicate.test(VanillaAssetGroup.TEXTURES);
    if (GrassColormapResourceLoader.class == type) return predicate.test(VanillaAssetGroup.TEXTURES);
    if (BakedModelManager.class == type) return predicate.test(VanillaAssetGroup.MODELS);
    if (ItemRenderer.class == type) return predicate.test(VanillaAssetGroup.MODELS);
    if (BlockRenderManager.class == type) return predicate.test(VanillaAssetGroup.MODELS);
    if (WorldRenderer.class == type) return predicate.test(VanillaAssetGroup.MODELS);
    if (SoundLoader.class == type) return predicate.test(VanillaAssetGroup.SOUNDS);
    if (GameRenderer.class == type) return predicate.test(VanillaAssetGroup.SHADERS);
    return true;
  }

  private static final class ReloadPredicate {
    private static final AtomicReference<Predicate<AssetGroup>> CURRENT = new AtomicReference<>();
    private static final Predicate<AssetGroup> DEFAULT = group -> true;

    private static Predicate<AssetGroup> currentOrDefault() {
      return MoreObjects.firstNonNull(CURRENT.get(), DEFAULT);
    }

    private static void run(final Runnable task, final Predicate<AssetGroup> predicate) {
      load(predicate);
      try {
        task.run();
      } finally {
        invalidate();
      }
    }

    private static void load(final Predicate<AssetGroup> predicate) {
      if (!CURRENT.compareAndSet(null, predicate)) {
        throw new ConcurrentModificationException("Already loaded");
      }
    }

    private static void invalidate() {
      if (CURRENT.get() == null) {
        throw new ConcurrentModificationException("Already invalidated");
      }
      CURRENT.set(null);
    }
  }
}
