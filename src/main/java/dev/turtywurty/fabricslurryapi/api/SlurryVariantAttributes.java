package dev.turtywurty.fabricslurryapi.api;

import dev.turtywurty.fabricslurryapi.FabricSlurryApi;
import net.fabricmc.fabric.api.lookup.v1.custom.ApiProviderMap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public final class SlurryVariantAttributes {
    private static final ApiProviderMap<Slurry, SlurryVariantAttributeHandler> HANDLERS = ApiProviderMap.create();
    private static final SlurryVariantAttributeHandler DEFAULT_HANDLER = slurryVariant -> {
        Holder<Slurry> registryEntry = slurryVariant.typeHolder();
        if (registryEntry.unwrapKey().isPresent()) {
            Identifier id = registryEntry.unwrapKey().get().identifier();
            return Component.translatable("slurry." + id.getNamespace() + "." + id.getPath());
        }

        return Component.translatable("slurry." + FabricSlurryApi.MOD_ID + ".unknown");
    };

    public static void register(Slurry slurry, SlurryVariantAttributeHandler handler) {
        if (HANDLERS.putIfAbsent(slurry, handler) != null) {
            throw new IllegalArgumentException("Duplicate handler registration for slurry " + slurry);
        }
    }

    @Nullable
    public static SlurryVariantAttributeHandler getHandler(Slurry slurry) {
        return HANDLERS.get(slurry);
    }

    public static SlurryVariantAttributeHandler getHandlerOrDefault(Slurry slurry) {
        SlurryVariantAttributeHandler handler = HANDLERS.get(slurry);
        return handler == null ? DEFAULT_HANDLER : handler;
    }

    public static Component getName(SlurryVariant slurryVariant) {
        return getHandlerOrDefault(slurryVariant.getSlurry()).getName(slurryVariant);
    }

    public static SoundEvent getFillSound(SlurryVariant slurryVariant, @Nullable Item handItem) {
        return getHandlerOrDefault(slurryVariant.getSlurry())
                .getFillSound(slurryVariant, handItem)
                .orElse(SoundEvents.BUCKET_FILL);
    }

    public static SoundEvent getEmptySound(SlurryVariant slurryVariant, @Nullable Item handItem) {
        return getHandlerOrDefault(slurryVariant.getSlurry())
                .getEmptySound(slurryVariant, handItem)
                .orElse(SoundEvents.BUCKET_EMPTY);
    }
}
