package dev.turtywurty.fabricslurryapi.api.storage;

import dev.turtywurty.fabricslurryapi.api.SlurryVariant;

import java.util.function.Predicate;

public class OutputSingleSlurryStorage extends PredicateSingleSlurryStorage {
    public OutputSingleSlurryStorage(long capacity, Predicate<SlurryVariant> canExtract) {
        super(capacity, $ -> false, canExtract);
    }

    public OutputSingleSlurryStorage(long capacity) {
        this(capacity, $ -> true);
    }
}
