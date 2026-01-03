package dev.turtywurty.fabricslurryapi.client.handler;

import dev.turtywurty.fabricslurryapi.api.SlurryVariant;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface SlurryVariantRenderHandler {
    default void appendTooltip(SlurryVariant variant, List<Component> tooltip, TooltipFlag tooltipType) {
    }

    @Nullable
    default TextureAtlasSprite getSprite(SlurryVariant variant) {
        SlurryRenderHandler handler = SlurryRenderHandlerRegistry.get(variant.getSlurry());
        return handler != null ? handler.getSprite(null, null) : null;
    }

    default int getColor(SlurryVariant variant, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
        SlurryRenderHandler handler = SlurryRenderHandlerRegistry.get(variant.getSlurry());
        return handler != null ? handler.getColor(view, pos) | 255 << 24 : -1;
    }
}
