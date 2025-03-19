package dev.turtywurty.fabricslurryapi.api.storage;

import dev.turtywurty.fabricslurryapi.api.SlurryVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

public class EmptySlurryStorage extends SingleSlurryStorage {
    public static final EmptySlurryStorage INSTANCE = new EmptySlurryStorage();

    @Override
    protected long getCapacity(SlurryVariant variant) {
        return 0;
    }

    @Override
    public long insert(SlurryVariant insertedVariant, long maxAmount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public long extract(SlurryVariant extractedVariant, long maxAmount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public long getAmount() {
        return 0;
    }

    @Override
    public boolean supportsInsertion() {
        return false;
    }

    @Override
    public boolean supportsExtraction() {
        return false;
    }
}
