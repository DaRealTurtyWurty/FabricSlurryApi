package dev.turtywurty.fabricslurryapi.api;

import dev.turtywurty.fabricslurryapi.FabricSlurryApi;
import net.minecraft.core.Holder;

import java.util.Objects;

public final class Slurry {
    private final String id;
    private final Holder.Reference<Slurry> builtInRegistryHolder = FabricSlurryApi.SLURRIES.createIntrusiveHolder(this);

    public Slurry(String id) {
        this.id = id;
    }

    public boolean matchesType(Slurry slurry) {
        return slurry == this;
    }

    public String id() {
        return id;
    }

    @Deprecated
    public Holder.Reference<Slurry> builtInRegistryHolder() {
        return this.builtInRegistryHolder;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Slurry) obj;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Slurry[" +
                "id=" + id + ']';
    }
}
