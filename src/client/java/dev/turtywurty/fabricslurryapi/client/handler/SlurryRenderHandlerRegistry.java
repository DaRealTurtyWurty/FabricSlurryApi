package dev.turtywurty.fabricslurryapi.client.handler;

import dev.turtywurty.fabricslurryapi.api.Slurry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.BlockPos;
import net.minecraft.data.AtlasIds;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

public final class SlurryRenderHandlerRegistry {
    private static final Map<Slurry, SlurryRenderHandler> HANDLERS = new IdentityHashMap<>();
    private static TextureAtlasSprite missingSprite;

    public static final SlurryRenderHandler MISSING_HANDLER = new SlurryRenderHandler() {
        @Override
        public TextureAtlasSprite getSprite(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
            if (missingSprite == null) {
                missingSprite = Minecraft.getInstance().getAtlasManager()
                        .get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, MissingTextureAtlasSprite.getLocation()));
            }

            return missingSprite;
        }
    };

    public static @Nullable SlurryRenderHandler get(Slurry slurry) {
        return HANDLERS.getOrDefault(slurry, MISSING_HANDLER);
    }

    public static void register(Slurry slurry, SlurryRenderHandler handler) {
        if (HANDLERS.putIfAbsent(slurry, handler) != null)
            throw new IllegalStateException("Duplicate handler for slurry: " + slurry);
    }

    public static void onResourcesReload() {
        TextureAtlas texture = Minecraft.getInstance()
                .getAtlasManager()
                .getAtlasOrThrow(AtlasIds.BLOCKS);

        HANDLERS.values().forEach(handler -> handler.reloadTextures(texture));
    }
}
