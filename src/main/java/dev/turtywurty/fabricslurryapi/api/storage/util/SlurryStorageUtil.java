package dev.turtywurty.fabricslurryapi.api.storage.util;

import dev.turtywurty.fabricslurryapi.api.SlurryVariant;
import dev.turtywurty.fabricslurryapi.api.SlurryVariantAttributes;
import dev.turtywurty.fabricslurryapi.api.storage.SlurryStorage;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.impl.transfer.DebugMessages;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.Objects;

public class SlurryStorageUtil {
    public static boolean interactWithSlurryStorage(Storage<SlurryVariant> storage, Player player, InteractionHand hand) {
        Storage<SlurryVariant> handStorage = ContainerItemContext.forPlayerInteraction(player, hand).find(SlurryStorage.ITEM);
        if (handStorage == null)
            return false;

        Item handItem = player.getItemInHand(hand).getItem();

        try {
            return moveWithSound(storage, handStorage, player, true, handItem) || moveWithSound(handStorage, storage, player, false, handItem);
        } catch (Exception e) {
            CrashReport report = CrashReport.forThrowable(e, "Interacting with slurry storage");
            report.addCategory("Interaction details")
                    .setDetail("Player", () -> DebugMessages.forPlayer(player))
                    .setDetail("Hand", hand)
                    .setDetail("Hand item", handItem::toString)
                    .setDetail("Slurry storage", () -> Objects.toString(storage, null));
            throw new ReportedException(report);
        }
    }

    private static boolean moveWithSound(Storage<SlurryVariant> from, Storage<SlurryVariant> to, Player player, boolean fill, Item handItem) {
        for (StorageView<SlurryVariant> view : from.nonEmptyViews()) {
            SlurryVariant resource = view.getResource();

            long maxExtracted;

            try (Transaction transaction = Transaction.openOuter()) {
                maxExtracted = view.extract(resource, Long.MAX_VALUE, transaction);
            }

            try (Transaction transaction = Transaction.openOuter()) {
                long accepted = to.insert(resource, maxExtracted, transaction);
                if (accepted > 0 && view.extract(resource, accepted, transaction) == accepted) {
                    transaction.commit();

                    SoundEvent sound = fill ? SlurryVariantAttributes.getFillSound(resource, handItem) : SlurryVariantAttributes.getEmptySound(resource, handItem);
                    if (sound != null) {
                        float pitch = 0.6F + player.getRandom().nextFloat() * 0.4F;
                        player.level().playSound(player, player.getX(), player.getY(), player.getZ(), sound, SoundSource.PLAYERS, 1.0F, pitch);
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
