package dev.turtywurty.fabricslurryapi.api.storage;

import dev.turtywurty.fabricslurryapi.api.SlurryVariant;

import java.util.function.Predicate;

public class InputSingleSlurryStorage extends PredicateSingleSlurryStorage {
    public InputSingleSlurryStorage(long capacity, Predicate<SlurryVariant> canInsert) {
        super(capacity, canInsert, $ -> false);
    }

    public InputSingleSlurryStorage(long capacity) {
        this(capacity, $ -> true);
    }
}
