package me.leeeaf.oakclient;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class OakClientClient implements ClientModInitializer {
    public static MinecraftClient mc;

    @Override
    public void onInitializeClient() {
        mc = MinecraftClient.getInstance();
    }
}
