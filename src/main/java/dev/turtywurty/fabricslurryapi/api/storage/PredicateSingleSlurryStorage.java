package dev.turtywurty.fabricslurryapi.api.storage;

import dev.turtywurty.fabricslurryapi.api.SlurryVariant;

import java.util.function.Predicate;

public class PredicateSingleSlurryStorage extends SingleSlurryStorage {
    private final long capacity;
    private final Predicate<SlurryVariant> canInsert;
    private final Predicate<SlurryVariant> canExtract;

    public PredicateSingleSlurryStorage(long capacity, Predicate<SlurryVariant> canInsert, Predicate<SlurryVariant> canExtract) {
        this.capacity = capacity;
        this.canInsert = canInsert;
        this.canExtract = canExtract;
    }

    public PredicateSingleSlurryStorage(long capacity, Predicate<SlurryVariant> isValid) {
        this(capacity, isValid, isValid);
    }

    @Override
    protected long getCapacity(SlurryVariant variant) {
        return this.capacity;
    }

    @Override
    protected boolean canInsert(SlurryVariant variant) {
        return super.canInsert(variant) && this.canInsert.test(variant);
    }

    @Override
    protected boolean canExtract(SlurryVariant variant) {
        return super.canExtract(variant) && this.canExtract.test(variant);
    }
}
