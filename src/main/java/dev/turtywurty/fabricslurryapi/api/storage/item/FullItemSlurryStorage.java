package dev.turtywurty.fabricslurryapi.api.storage.item;

import dev.turtywurty.fabricslurryapi.api.SlurryVariant;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ExtractionOnlyStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.item.Item;

import java.util.function.Function;

public class FullItemSlurryStorage implements ExtractionOnlyStorage<SlurryVariant>, SingleSlotStorage<SlurryVariant> {
    private final ContainerItemContext context;
    private final Item fullItem;
    private final Function<ItemVariant, ItemVariant> fullToEmptyMapping;
    private final SlurryVariant containedSlurry;
    private final long containedAmount;

    public FullItemSlurryStorage(ContainerItemContext context, Item fullItem, SlurryVariant containedSlurry, long containedAmount) {
        this(context, fullVariant -> ItemVariant.of(fullItem, fullVariant.getComponents()), containedSlurry, containedAmount);
    }

    public FullItemSlurryStorage(ContainerItemContext context, Function<ItemVariant, ItemVariant> fullToEmptyMapping, SlurryVariant containedSlurry, long containedAmount) {
        StoragePreconditions.notBlankNotNegative(containedSlurry, containedAmount);

        this.context = context;
        this.fullItem = context.getItemVariant().getItem();
        this.fullToEmptyMapping = fullToEmptyMapping;
        this.containedSlurry = containedSlurry;
        this.containedAmount = containedAmount;
    }

    @Override
    public long extract(SlurryVariant resource, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(resource, maxAmount);

        if(!context.getItemVariant().isOf(fullItem)) return 0;

        if(resource.equals(containedSlurry) && maxAmount >= containedAmount) {
            ItemVariant emptyItem = fullToEmptyMapping.apply(context.getItemVariant());
            if(context.exchange(emptyItem, 1, transaction) == 1) {
                return containedAmount;
            }
        }

        return 0;
    }

    @Override
    public boolean isResourceBlank() {
        return getResource().isBlank();
    }

    @Override
    public SlurryVariant getResource() {
        return context.getItemVariant().isOf(fullItem) ? containedSlurry : SlurryVariant.blank();
    }

    @Override
    public long getAmount() {
        return context.getItemVariant().isOf(fullItem) ? containedAmount : 0;
    }

    @Override
    public long getCapacity() {
        return getAmount();
    }

    @Override
    public String toString() {
        return "FullItemSlurryStorage{" +
                "context=" + context +
                ", fullItem=" + fullItem +
                ", containedSlurry=" + containedSlurry +
                ", containedAmount=" + containedAmount +
                '}';
    }
}
