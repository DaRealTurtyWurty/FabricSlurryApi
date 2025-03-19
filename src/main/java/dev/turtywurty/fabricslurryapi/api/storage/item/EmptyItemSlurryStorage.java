package dev.turtywurty.fabricslurryapi.api.storage.item;

import dev.turtywurty.fabricslurryapi.api.Slurry;
import dev.turtywurty.fabricslurryapi.api.SlurryVariant;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.BlankVariantView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.InsertionOnlyStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class EmptyItemSlurryStorage implements InsertionOnlyStorage<SlurryVariant> {
    private final ContainerItemContext context;
    private final Item emptyItem;
    private final Function<ItemVariant, ItemVariant> emptyToFullMapping;
    private final Slurry insertableSlurry;
    private final long insertableAmount;
    private final List<StorageView<SlurryVariant>> blankView;

    public EmptyItemSlurryStorage(ContainerItemContext context, Item fullItem, Slurry insertableSlurry, long insertableAmount) {
        this(context, emptyVariant -> ItemVariant.of(fullItem, emptyVariant.getComponents()), insertableSlurry, insertableAmount);
    }

    public EmptyItemSlurryStorage(ContainerItemContext context, Function<ItemVariant, ItemVariant> emptyToFullMapping, Slurry insertableSlurry, long insertableAmount) {
        StoragePreconditions.notNegative(insertableAmount);

        this.context = context;
        this.emptyItem = context.getItemVariant().getItem();
        this.emptyToFullMapping = emptyToFullMapping;
        this.insertableSlurry = insertableSlurry;
        this.insertableAmount = insertableAmount;
        this.blankView = List.of(new BlankVariantView<>(SlurryVariant.blank(), insertableAmount));
    }

    @Override
    public long insert(SlurryVariant resource, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(resource, maxAmount);

        if(!context.getItemVariant().isOf(emptyItem)) return 0;

        if(resource.isOf(insertableSlurry) && maxAmount >= insertableAmount) {
            ItemVariant fullItem = emptyToFullMapping.apply(context.getItemVariant());
            if(context.exchange(fullItem, 1, transaction) == 1) {
                return insertableAmount;
            }
        }

        return 0;
    }

    @Override
    public @NotNull Iterator<StorageView<SlurryVariant>> iterator() {
        return blankView.iterator();
    }

    @Override
    public String toString() {
        return "EmptyItemSlurryStorage{" +
                "context=" + context +
                ", emptyItem=" + emptyItem +
                ", insertableSlurry=" + insertableSlurry +
                ", insertableAmount=" + insertableAmount +
                '}';
    }
}
