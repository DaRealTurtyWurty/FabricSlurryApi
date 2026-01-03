package dev.turtywurty.fabricslurryapi.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.turtywurty.fabricslurryapi.FabricSlurryApi;
import dev.turtywurty.fabricslurryapi.impl.SlurryVariantImpl;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface SlurryVariant extends TransferVariant<Slurry> {
    Codec<SlurryVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FabricSlurryApi.SLURRIES.holderByNameCodec().fieldOf("slurry").forGetter(SlurryVariant::typeHolder),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(SlurryVariant::getComponentsPatch)
    ).apply(instance, SlurryVariantImpl::of));

    StreamCodec<RegistryFriendlyByteBuf, SlurryVariant> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(FabricSlurryApi.SLURRIES_REGISTRY_KEY), SlurryVariant::typeHolder,
            DataComponentPatch.STREAM_CODEC, SlurryVariant::getComponentsPatch,
            SlurryVariantImpl::of);

    static SlurryVariant blank() {
        return of(FabricSlurryApi.EMPTY);
    }

    static SlurryVariant of(Slurry slurry) {
        return of(slurry, DataComponentPatch.EMPTY);
    }

    static SlurryVariant of(Slurry slurry, DataComponentPatch componentChanges) {
        return SlurryVariantImpl.of(slurry, componentChanges);
    }

    default Slurry getSlurry() {
        return getObject();
    }

    @Override
    default Holder<Slurry> typeHolder() {
        return getSlurry().builtInRegistryHolder();
    }
}
