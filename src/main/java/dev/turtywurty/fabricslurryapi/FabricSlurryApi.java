package dev.turtywurty.fabricslurryapi;

import dev.turtywurty.fabricslurryapi.api.Slurry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class FabricSlurryApi implements ModInitializer {
    public static final String MOD_ID = "slurryapi";

    public static final RegistryKey<Registry<Slurry>> SLURRIES_REGISTRY_KEY = RegistryKey.ofRegistry(id("slurries"));
    public static final DefaultedRegistry<Slurry> SLURRIES = FabricRegistryBuilder.createDefaulted(SLURRIES_REGISTRY_KEY, FabricSlurryApi.id("empty"))
            .attribute(RegistryAttribute.MODDED)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    public static final Slurry EMPTY = register("empty");

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    @Override
    public void onInitialize() {}

    public static Slurry register(String id) {
        Identifier identifier = id(id);
        return register(identifier);
    }

    public static Slurry register(Identifier id) {
        return Registry.register(SLURRIES, id, new Slurry(id.toString()));
    }
}
