package dev.turtywurty.fabricslurryapi.impl;

import dev.turtywurty.fabricslurryapi.FabricSlurryApi;
import dev.turtywurty.fabricslurryapi.api.Slurry;
import dev.turtywurty.fabricslurryapi.api.SlurryVariant;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;

import java.util.Objects;

public final class SlurryVariantImpl implements SlurryVariant {
    public static SlurryVariant of(Slurry slurry, DataComponentPatch components) {
        Objects.requireNonNull(slurry, "Slurry may not be null.");
        Objects.requireNonNull(components, "Components may not be null.");

        return new SlurryVariantImpl(slurry, components);
    }

    public static SlurryVariant of(Holder<Slurry> slurry, DataComponentPatch components) {
        Objects.requireNonNull(slurry, "Slurry may not be null.");

        return of(slurry.value(), components);
    }

    private final Slurry slurry;
    private final DataComponentPatch components;
    private final DataComponentMap componentMap;
    private final int hashCode;

    private SlurryVariantImpl(Slurry slurry, DataComponentPatch components) {
        this.slurry = slurry;
        this.components = components;
        this.componentMap = components == DataComponentPatch.EMPTY ? DataComponentMap.EMPTY : PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, components);
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
    public DataComponentPatch getComponentsPatch() {
        return this.components;
    }

    @Override
    public DataComponentMap getComponents() {
        return this.componentMap;
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
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        SlurryVariantImpl that = (SlurryVariantImpl) obj;
        return this.hashCode == that.hashCode && this.slurry.matchesType(that.slurry) && componentsMatch(that.components);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }
}
