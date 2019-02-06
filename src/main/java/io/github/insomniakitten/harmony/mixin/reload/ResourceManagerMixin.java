package io.github.insomniakitten.harmony.mixin.reload;

import io.github.insomniakitten.harmony.Harmony;
import io.github.insomniakitten.harmony.reload.AssetReloader;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ReloadableResourceManagerImpl.class)
abstract class ResourceManagerMixin {
  private ResourceManagerMixin() {}

  @Shadow
  protected abstract String emitReloadTimed(final ResourceReloadListener listener);

  @Redirect(
    method = "reloadAll",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/resource/ReloadableResourceManagerImpl;emitReloadTimed(Lnet/minecraft/resource/ResourceReloadListener;)Ljava/lang/String;"
    )
  )
  private String reloadAllEmitReloadTimed(final ReloadableResourceManagerImpl manager, final ResourceReloadListener listener) {
    return AssetReloader.canReload(listener) ? this.emitReloadTimed(listener) : Harmony.SKIP_MARKER;
  }

  @Redirect(
    method = "reloadAll",
    at = @At(
      value = "INVOKE",
      target = "Ljava/util/List;add(Ljava/lang/Object;)Z"
    )
  )
  private boolean reloadAllListAdd(final List<String> list, final Object value) {
    if (Harmony.SKIP_MARKER.equals(value)) return false;
    list.add((String) value);
    return true;
  }

  @Redirect(
    method = "emitReloadAll",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/resource/ResourceReloadListener;onResourceReload(Lnet/minecraft/resource/ResourceManager;)V"
    )
  )
  private void emitReloadAllOnResourceReload(final ResourceReloadListener listener, final ResourceManager manager) {
    if (AssetReloader.canReload(listener)) listener.onResourceReload(manager);
  }

  @Redirect(
    method = "emitReloadTimed",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/resource/ResourceReloadListener;onResourceReload(Lnet/minecraft/resource/ResourceManager;)V"
    )
  )
  private void emitReloadTimedOnResourceReload(final ResourceReloadListener listener, final ResourceManager manager) {
    if (AssetReloader.canReload(listener)) listener.onResourceReload(manager);
  }
}
