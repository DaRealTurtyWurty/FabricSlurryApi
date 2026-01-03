package dev.turtywurty.fabricslurryapi;

import com.mojang.serialization.Lifecycle;
import dev.turtywurty.fabricslurryapi.api.Slurry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.DefaultedMappedRegistry;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class FabricSlurryApi implements ModInitializer {
    public static final String MOD_ID = "slurryapi";

    public static final ResourceKey<Registry<Slurry>> SLURRIES_REGISTRY_KEY = ResourceKey.createRegistryKey(id("slurries"));
    public static final DefaultedRegistry<Slurry> SLURRIES = FabricRegistryBuilder.from(new DefaultedMappedRegistry<>(FabricSlurryApi.id("empty").toString(), SLURRIES_REGISTRY_KEY, Lifecycle.stable(), true))
            .attribute(RegistryAttribute.MODDED)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    public static final Slurry EMPTY = register("empty");

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
    }

    public static Slurry register(String id) {
        Identifier identifier = id(id);
        return register(identifier);
    }

    public static Slurry register(Identifier id) {
        return Registry.register(SLURRIES, id, new Slurry(id.toString()));
    }
}
