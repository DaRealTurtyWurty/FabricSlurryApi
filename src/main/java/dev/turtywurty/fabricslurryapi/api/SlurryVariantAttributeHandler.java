package dev.turtywurty.fabricslurryapi.api;

import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface SlurryVariantAttributeHandler {
    Text getName(SlurryVariant slurryVariant);

    default Optional<SoundEvent> getFillSound(SlurryVariant slurryVariant, @Nullable Item handItem) {
        return Optional.empty();
    }

    default Optional<SoundEvent> getEmptySound(SlurryVariant slurryVariant, @Nullable Item handItem) {
        return Optional.empty();
    }
}
