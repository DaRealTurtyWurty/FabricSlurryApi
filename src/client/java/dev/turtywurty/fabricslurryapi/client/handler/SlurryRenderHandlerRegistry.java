package dev.turtywurty.fabricslurryapi.client.handler;

import dev.turtywurty.fabricslurryapi.api.Slurry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.SpriteAtlasTexture;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

public final class SlurryRenderHandlerRegistry {
    private static final Map<Slurry, SlurryRenderHandler> HANDLERS = new IdentityHashMap<>();

    public static @Nullable SlurryRenderHandler get(Slurry slurry) {
        return HANDLERS.get(slurry);
    }

    public static void register(Slurry slurry, SlurryRenderHandler handler) {
        if(HANDLERS.putIfAbsent(slurry, handler) != null) {
            throw new IllegalStateException("Duplicate handler for slurry: " + slurry);
        }
    }

    public static void onResourcesReload() {
        SpriteAtlasTexture texture = MinecraftClient.getInstance()
                .getBakedModelManager()
                .getAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);

        HANDLERS.values().forEach(handler -> handler.reloadTextures(texture));
    }
}
