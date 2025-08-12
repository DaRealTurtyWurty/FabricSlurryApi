package dev.turtywurty.fabricslurryapi.api;

import dev.turtywurty.fabricslurryapi.FabricSlurryApi;
import net.fabricmc.fabric.api.lookup.v1.custom.ApiProviderMap;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public final class SlurryVariantAttributes {
    private static final ApiProviderMap<Slurry, SlurryVariantAttributeHandler> HANDLERS = ApiProviderMap.create();
    private static final SlurryVariantAttributeHandler DEFAULT_HANDLER = slurryVariant -> {
        RegistryEntry<Slurry> registryEntry = slurryVariant.getRegistryEntry();
        if (registryEntry.getKey().isPresent()) {
            Identifier id = registryEntry.getKey().get().getValue();
            return Text.translatable("slurry." + id.getNamespace() + "." + id.getPath());
        }

        return Text.translatable("slurry." + FabricSlurryApi.MOD_ID + ".unknown");
    };

    public static void register(Slurry slurry, SlurryVariantAttributeHandler handler) {
        if(HANDLERS.putIfAbsent(slurry, handler) != null) {
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

    public static Text getName(SlurryVariant slurryVariant) {
        return getHandlerOrDefault(slurryVariant.getSlurry()).getName(slurryVariant);
    }

    public static SoundEvent getFillSound(SlurryVariant slurryVariant, @Nullable Item handItem) {
        return getHandlerOrDefault(slurryVariant.getSlurry())
                .getFillSound(slurryVariant, handItem)
                .orElse(SoundEvents.ITEM_BUCKET_FILL);
    }

    public static SoundEvent getEmptySound(SlurryVariant slurryVariant, @Nullable Item handItem) {
        return getHandlerOrDefault(slurryVariant.getSlurry())
                .getEmptySound(slurryVariant, handItem)
                .orElse(SoundEvents.ITEM_BUCKET_EMPTY);
    }
}
