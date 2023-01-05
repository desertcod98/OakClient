package me.leeeaf.oakclient.event.events.render;

import me.leeeaf.oakclient.event.Event;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class RenderEntityLabelEvent extends Event {
    public Entity entity;
    public MatrixStack matrices;
    public int light;
    public VertexConsumerProvider vertexConsumers;
    public EntityRenderDispatcher dispatcher;

    public RenderEntityLabelEvent(Entity entity, MatrixStack matrices, int light, VertexConsumerProvider vertexConsumers, EntityRenderDispatcher dispatcher) {
        this.entity = entity;
        this.matrices = matrices;
        this.light = light;
        this.vertexConsumers = vertexConsumers;
        this.dispatcher = dispatcher;
    }
}
