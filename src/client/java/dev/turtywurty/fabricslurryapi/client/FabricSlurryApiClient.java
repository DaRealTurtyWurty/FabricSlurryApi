package dev.turtywurty.fabricslurryapi.client;

import dev.turtywurty.fabricslurryapi.FabricSlurryApi;
import dev.turtywurty.fabricslurryapi.client.handler.SlurryRenderHandlerRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class FabricSlurryApiClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(
                new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() {
                        return FabricSlurryApi.id("slurry_textures");
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        SlurryRenderHandlerRegistry.onResourcesReload();
                    }
                });
    }
}
