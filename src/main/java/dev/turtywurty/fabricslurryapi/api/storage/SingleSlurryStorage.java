package dev.turtywurty.fabricslurryapi.api.storage;

import dev.turtywurty.fabricslurryapi.api.SlurryVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

import java.util.Objects;

public abstract class SingleSlurryStorage extends SingleVariantStorage<SlurryVariant> {
    public static SingleSlurryStorage withFixedCapacity(long capacity, Runnable onChange) {
        StoragePreconditions.notNegative(capacity);
        Objects.requireNonNull(onChange, "onChange may not be null");

        return new SingleSlurryStorage() {
            @Override
            protected long getCapacity(SlurryVariant variant) {
                return capacity;
            }

            @Override
            protected void onFinalCommit() {
                onChange.run();
            }
        };
    }

    @Override
    protected SlurryVariant getBlankVariant() {
        return SlurryVariant.blank();
    }

    public void readData(ReadView view) {
        SingleVariantStorage.readData(this, SlurryVariant.CODEC, SlurryVariant::blank, view);
    }

    public void writeNbt(WriteView view) {
        SingleVariantStorage.writeData(this, SlurryVariant.CODEC, view);
    }
}
