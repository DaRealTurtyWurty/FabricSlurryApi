package dev.turtywurty.fabricslurryapi.client.handler;

import net.minecraft.client.render.model.SpriteAtlasManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

public class SimpleSlurryRenderHandler implements SlurryRenderHandler {
    protected final Identifier texture;
    protected final Sprite[] sprites = new Sprite[1];
    protected final int tint;

    public SimpleSlurryRenderHandler(Identifier texture, int tint) {
        this.texture = texture;
        this.tint = tint;
    }

    public SimpleSlurryRenderHandler(Identifier texture) {
        this(texture, -1);
    }

    @Override
    public Sprite getSprite(@Nullable BlockRenderView view, @Nullable BlockPos pos) {
        return sprites[0];
    }

    @Override
    public void reloadTextures(SpriteAtlasTexture textureAtlas) {
        sprites[0] = textureAtlas.getSprite(texture);
    }

    @Override
    public int getColor(@Nullable BlockRenderView view, @Nullable BlockPos pos) {
        return tint;
    }
}
