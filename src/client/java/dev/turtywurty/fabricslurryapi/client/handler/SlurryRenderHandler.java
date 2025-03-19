package dev.turtywurty.fabricslurryapi.client.handler;

import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

public interface SlurryRenderHandler {
    Sprite getSprite(@Nullable BlockRenderView view, @Nullable BlockPos pos);

    default int getColor(@Nullable BlockRenderView view, @Nullable BlockPos pos) {
        return -1;
    }

    default void reloadTextures(SpriteAtlasTexture textureAtlas) {}
}
