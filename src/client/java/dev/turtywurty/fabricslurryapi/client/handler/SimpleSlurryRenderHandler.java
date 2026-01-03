package dev.turtywurty.fabricslurryapi.client.handler;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

public class SimpleSlurryRenderHandler implements SlurryRenderHandler {
    protected final Identifier texture;
    protected final TextureAtlasSprite[] sprites = new TextureAtlasSprite[1];
    protected final int tint;

    public SimpleSlurryRenderHandler(Identifier texture, int tint) {
        this.texture = texture;
        this.tint = tint;
    }

    public SimpleSlurryRenderHandler(Identifier texture) {
        this(texture, -1);
    }

        @Override
        public TextureAtlasSprite getSprite(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
            return sprites[0];
    }

    @Override
    public void reloadTextures(TextureAtlas textureAtlas) {
        sprites[0] = textureAtlas.getSprite(texture);
    }

    @Override
    public int getColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
        return tint;
    }
}
