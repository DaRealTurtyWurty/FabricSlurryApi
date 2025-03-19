package dev.turtywurty.fabricslurryapi.api;

public record Slurry(String id) {
    public boolean matchesType(Slurry slurry) {
        return slurry == this;
    }
}
