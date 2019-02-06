/*
 * Copyright (C) 2018 InsomniaKitten
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.insomniakitten.harmony.mixin.threading;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.ChunkRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(WorldRenderer.class)
abstract class WorldRendererMixin {
  private WorldRendererMixin() {}

  @Redirect(
    method = "setUpTerrain",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/render/chunk/ChunkRenderer;method_3661()Z"
    )
  )
  private boolean setUpTerrain$method_3661(final ChunkRenderer renderer) {
    return false;
  }

  @ModifyVariable(
    method = "setUpTerrain",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/render/chunk/ChunkRenderer;method_3661()Z",
      shift = At.Shift.BEFORE
    ),
    slice = @Slice(
      from = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/render/chunk/ChunkRenderer;getOrigin()Lnet/minecraft/util/math/BlockPos;"
      ),
      to = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/render/chunk/ChunkRenderer;method_3661()Z"
      )
    ),
    ordinal = 1
  )
  private boolean setUpTerrain$boolean(final boolean isInRange) {
    return false;
  }
}
