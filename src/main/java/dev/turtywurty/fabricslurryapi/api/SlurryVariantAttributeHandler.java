package dev.turtywurty.fabricslurryapi.api;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface SlurryVariantAttributeHandler {
    Component getName(SlurryVariant slurryVariant);

    default Optional<SoundEvent> getFillSound(SlurryVariant slurryVariant, @Nullable Item handItem) {
        return Optional.empty();
    }

    default Optional<SoundEvent> getEmptySound(SlurryVariant slurryVariant, @Nullable Item handItem) {
        return Optional.empty();
    }
}
