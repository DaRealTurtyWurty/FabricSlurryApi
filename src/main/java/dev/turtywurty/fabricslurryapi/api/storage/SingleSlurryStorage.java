package dev.turtywurty.fabricslurryapi.api.storage;

import dev.turtywurty.fabricslurryapi.api.SlurryVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

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

    public void readData(ValueInput view) {
        SingleVariantStorage.readValue(this, SlurryVariant.CODEC, SlurryVariant::blank, view);
    }

    public void writeNbt(ValueOutput view) {
        SingleVariantStorage.writeValue(this, SlurryVariant.CODEC, view);
    }
}
