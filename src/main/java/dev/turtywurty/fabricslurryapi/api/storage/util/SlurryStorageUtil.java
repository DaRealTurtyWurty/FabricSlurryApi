package dev.turtywurty.fabricslurryapi.api.storage.util;

import dev.turtywurty.fabricslurryapi.api.SlurryVariant;
import dev.turtywurty.fabricslurryapi.api.SlurryVariantAttributes;
import dev.turtywurty.fabricslurryapi.api.storage.SlurryStorage;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.impl.transfer.DebugMessages;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;

import java.util.Objects;

public class SlurryStorageUtil {
    public static boolean interactWithSlurryStorage(Storage<SlurryVariant> storage, PlayerEntity player, Hand hand) {
        Storage<SlurryVariant> handStorage = ContainerItemContext.forPlayerInteraction(player, hand).find(SlurryStorage.ITEM);
        if (handStorage == null)
            return false;

        Item handItem = player.getStackInHand(hand).getItem();

        try {
            return moveWithSound(storage, handStorage, player, true, handItem) || moveWithSound(handStorage, storage, player, false, handItem);
        } catch (Exception e) {
            CrashReport report = CrashReport.create(e, "Interacting with slurry storage");
            report.addElement("Interaction details")
                    .add("Player", () -> DebugMessages.forPlayer(player))
                    .add("Hand", hand)
                    .add("Hand item", handItem::toString)
                    .add("Slurry storage", () -> Objects.toString(storage, null));
            throw new CrashException(report);
        }
    }

    private static boolean moveWithSound(Storage<SlurryVariant> from, Storage<SlurryVariant> to, PlayerEntity player, boolean fill, Item handItem) {
        for (StorageView<SlurryVariant> view : from.nonEmptyViews()) {
            SlurryVariant resource = view.getResource();

            long maxExtracted;

            try(Transaction transaction = Transaction.openOuter()) {
                maxExtracted = view.extract(resource, Long.MAX_VALUE, transaction);
            }

            try(Transaction transaction = Transaction.openOuter()) {
                long accepted = to.insert(resource, maxExtracted, transaction);
                if(accepted > 0 && view.extract(resource, accepted, transaction) == accepted) {
                    transaction.commit();

                    SoundEvent sound = fill ? SlurryVariantAttributes.getFillSound(resource, handItem) : SlurryVariantAttributes.getEmptySound(resource, handItem);
                    if(sound != null) {
                        float pitch = 0.6F + player.getRandom().nextFloat() * 0.4F;
                        player.getWorld().playSound(player, player.getX(), player.getY(), player.getZ(), sound, SoundCategory.PLAYERS, 1.0F, pitch);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private SlurryStorageUtil() {
        throw new AssertionError();
    }
}
