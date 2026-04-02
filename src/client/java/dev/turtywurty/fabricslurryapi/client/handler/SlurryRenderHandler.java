package dev.turtywurty.fabricslurryapi.client.handler;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface SlurryRenderHandler {
    TextureAtlasSprite getSprite(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos);

    default int getColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
        return -1;
    }

    default void reloadTextures(TextureAtlas textureAtlas) {
    }
}
