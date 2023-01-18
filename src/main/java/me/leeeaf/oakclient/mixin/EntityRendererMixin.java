package me.leeeaf.oakclient.mixin;

import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.events.render.RenderEntityLabelEvent;
import me.leeeaf.oakclient.event.events.render.RenderLabelIfPresentEvent;
import me.leeeaf.oakclient.systems.social.Relationship;
import me.leeeaf.oakclient.systems.social.SocialManager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin <T extends Entity>{

    @Final
    @Shadow protected EntityRenderDispatcher dispatcher;

    @Inject(method = "renderLabelIfPresent", at=@At("HEAD"))
    private void onRenderLabelIfPresent(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
        if(entity.isAlive()){
            EventBus.getEventBus().post(new RenderLabelIfPresentEvent(text, (LivingEntity) entity));
        }
    }

    @Inject(method = "render", at=@At("HEAD"))
    void onEntityLabelRender(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
        EventBus.getEventBus().post(new RenderEntityLabelEvent(entity, matrices, light, vertexConsumers, dispatcher));
    }

}
