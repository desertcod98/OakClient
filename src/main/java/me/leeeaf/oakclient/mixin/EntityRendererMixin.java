package me.leeeaf.oakclient.mixin;

import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.events.RenderLableIfPresentEvent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin <T extends Entity>{
    @Inject(method = "renderLabelIfPresent", at=@At("HEAD"))
    private void onRenderLabelIfPresent(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
        if(entity.isAlive()){
            EventBus.getEventBus().post(new RenderLableIfPresentEvent(text, (LivingEntity) entity));
        }
    }
}
