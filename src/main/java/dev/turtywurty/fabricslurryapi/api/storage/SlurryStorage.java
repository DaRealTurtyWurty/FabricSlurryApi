package dev.turtywurty.fabricslurryapi.api.storage;

import dev.turtywurty.fabricslurryapi.FabricSlurryApi;
import dev.turtywurty.fabricslurryapi.api.SlurryVariant;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class SlurryStorage {
    public static final BlockApiLookup<Storage<SlurryVariant>, @Nullable Direction> SIDED =
            BlockApiLookup.get(FabricSlurryApi.id("sided_slurry_storage"), Storage.asClass(), Direction.class);

    public static final ItemApiLookup<Storage<SlurryVariant>, ContainerItemContext> ITEM =
            ItemApiLookup.get(FabricSlurryApi.id("item_slurry_storage"), Storage.asClass(), ContainerItemContext.class);

    public static final Supplier<Storage<SlurryVariant>> EMPTY = () -> Objects.requireNonNull(EmptySlurryStorage.INSTANCE);

    public static Event<CombinedItemApiProvider> combinedItemApiProvider(Item item) {
        return CombinedProvidersImpl.getOrCreateItemEvent(item);
    }

    public static final Event<CombinedItemApiProvider> GENERAL_COMBINED_PROVIDER = CombinedProvidersImpl.createEvent(false);

    private SlurryStorage() {}

    static {
        SlurryStorage.ITEM.registerFallback((stack, context) -> GENERAL_COMBINED_PROVIDER.invoker().find(context));
    }

    @FunctionalInterface
    public interface CombinedItemApiProvider {
        Storage<SlurryVariant> find(ContainerItemContext context);
    }

    public static class CombinedProvidersImpl {
        public static Event<CombinedItemApiProvider> createEvent(boolean invokeFallback) {
            return EventFactory.createArrayBacked(CombinedItemApiProvider.class, listeners -> context -> {
                List<Storage<SlurryVariant>> storages = new ArrayList<>();

                for (CombinedItemApiProvider listener : listeners) {
                    Storage<SlurryVariant> found = listener.find(context);

                    if (found != null) {
                        storages.add(found);
                    }
                }

                if (!storages.isEmpty() && invokeFallback) {
                    Storage<SlurryVariant> fallbackFound = SlurryStorage.GENERAL_COMBINED_PROVIDER.invoker().find(context);

                    if (fallbackFound != null) {
                        storages.add(fallbackFound);
                    }
                }

                return storages.isEmpty() ? null : new CombinedStorage<>(storages);
            });
        }

        private static class Provider implements ItemApiLookup.ItemApiProvider<Storage<SlurryVariant>, ContainerItemContext> {
            private final Event<CombinedItemApiProvider> event = createEvent(true);

            @Override
            @Nullable
            public Storage<SlurryVariant> find(ItemStack itemStack, ContainerItemContext context) {
                if (!context.getItemVariant().matches(itemStack)) {
                    String errorMessage = String.format(
                            "Query stack %s and ContainerItemContext variant %s don't match.",
                            itemStack,
                            context.getItemVariant()
                    );
                    throw new IllegalArgumentException(errorMessage);
                }

                return event.invoker().find(context);
            }
        }

        public static Event<CombinedItemApiProvider> getOrCreateItemEvent(Item item) {
            ItemApiLookup.ItemApiProvider<Storage<SlurryVariant>, ContainerItemContext> existingProvider = SlurryStorage.ITEM.getProvider(item);

            if (existingProvider == null) {
                SlurryStorage.ITEM.registerForItems(new CombinedProvidersImpl.Provider(), item);
                existingProvider = SlurryStorage.ITEM.getProvider(item);
            }

            if (existingProvider instanceof CombinedProvidersImpl.Provider registeredProvider) {
                return registeredProvider.event;
            } else {
                String errorMessage = String.format(
                        "An incompatible provider was already registered for item %s. Provider: %s.",
                        item,
                        existingProvider
                );
                throw new IllegalStateException(errorMessage);
            }
        }
    }
}
