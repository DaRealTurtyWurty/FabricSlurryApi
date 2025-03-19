package dev.turtywurty.fabricslurryapi.client.handler;

import dev.turtywurty.fabricslurryapi.api.SlurryVariant;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface SlurryVariantRenderHandler {
    default void appendTooltip(SlurryVariant variant, List<Text> tooltip, TooltipType tooltipType) {}

    @Nullable
    default Sprite getSprite(SlurryVariant variant) {
        SlurryRenderHandler handler = SlurryRenderHandlerRegistry.get(variant.getSlurry());
        return handler != null ? handler.getSprite(null, null) : null;
    }

    default int getColor(SlurryVariant variant, @Nullable BlockRenderView view, @Nullable BlockPos pos) {
        SlurryRenderHandler handler = SlurryRenderHandlerRegistry.get(variant.getSlurry());
        return handler != null ? handler.getColor(view, pos) | 255 << 24 : -1;
    }
}
