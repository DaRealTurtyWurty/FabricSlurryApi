package dev.turtywurty.fabricslurryapi.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.turtywurty.fabricslurryapi.FabricSlurryApi;
import dev.turtywurty.fabricslurryapi.impl.SlurryVariantImpl;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.minecraft.component.ComponentChanges;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface SlurryVariant extends TransferVariant<Slurry> {
    Codec<SlurryVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FabricSlurryApi.SLURRIES.getEntryCodec().fieldOf("slurry").forGetter(SlurryVariant::getRegistryEntry),
            ComponentChanges.CODEC.optionalFieldOf("components", ComponentChanges.EMPTY).forGetter(SlurryVariant::getComponents)
    ).apply(instance, SlurryVariantImpl::of));

    PacketCodec<RegistryByteBuf, SlurryVariant> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.registryEntry(FabricSlurryApi.SLURRIES_REGISTRY_KEY), SlurryVariant::getRegistryEntry,
            ComponentChanges.PACKET_CODEC, SlurryVariant::getComponents,
            SlurryVariantImpl::of);

    static SlurryVariant blank() {
        return of(FabricSlurryApi.EMPTY);
    }

    static SlurryVariant of(Slurry slurry) {
        return of(slurry, ComponentChanges.EMPTY);
    }

    static SlurryVariant of(Slurry slurry, ComponentChanges componentChanges) {
        return SlurryVariantImpl.of(slurry, componentChanges);
    }

    default Slurry getSlurry() {
        return getObject();
    }

    default RegistryEntry<Slurry> getRegistryEntry() {
        return FabricSlurryApi.SLURRIES.getEntry(getSlurry());
    }
}
