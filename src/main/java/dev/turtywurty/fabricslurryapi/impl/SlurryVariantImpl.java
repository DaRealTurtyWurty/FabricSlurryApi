package dev.turtywurty.fabricslurryapi.impl;

import dev.turtywurty.fabricslurryapi.FabricSlurryApi;
import dev.turtywurty.fabricslurryapi.api.Slurry;
import dev.turtywurty.fabricslurryapi.api.SlurryVariant;
import net.minecraft.component.ComponentChanges;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Objects;

public final class SlurryVariantImpl implements SlurryVariant {
    public static SlurryVariant of(Slurry slurry, ComponentChanges components) {
        Objects.requireNonNull(slurry, "Slurry may not be null.");
        Objects.requireNonNull(components, "Components may not be null.");

        return new SlurryVariantImpl(slurry, components);
    }

    public static SlurryVariant of(RegistryEntry<Slurry> slurry, ComponentChanges components) {
        Objects.requireNonNull(slurry, "Slurry may not be null.");

        return of(slurry.value(), components);
    }

    private final Slurry slurry;
    private final ComponentChanges components;
    private final int hashCode;

    private SlurryVariantImpl(Slurry slurry, ComponentChanges components) {
        this.slurry = slurry;
        this.components = components;
        this.hashCode = Objects.hash(slurry, components);
    }

    @Override
    public boolean isBlank() {
        return this.slurry.matchesType(FabricSlurryApi.EMPTY);
    }

    @Override
    public Slurry getObject() {
        return this.slurry;
    }

    @Override
    public ComponentChanges getComponents() {
        return this.components;
    }

    @Override
    public String toString() {
        return "SlurryVariantImpl{" +
                "slurry=" + slurry +
                ", components=" + components +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        SlurryVariantImpl that = (SlurryVariantImpl) obj;
        return this.hashCode == that.hashCode && this.slurry.matchesType(that.slurry) && componentsMatch(that.components);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }
}
