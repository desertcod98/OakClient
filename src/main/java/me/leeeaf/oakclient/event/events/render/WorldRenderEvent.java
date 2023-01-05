package me.leeeaf.oakclient.event.events.render;

import net.minecraft.client.util.math.MatrixStack;

public class WorldRenderEvent {


    public static class Pre extends WorldRenderEvent {
        public Pre(float partialTicks, MatrixStack matrices) {
        }
    }

    public static class Post extends WorldRenderEvent {

    }
}
