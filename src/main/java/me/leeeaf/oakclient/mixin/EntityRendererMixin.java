package me.leeeaf.oakclient.mixin;

import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.events.render.RenderEntityLabelEvent;
import me.leeeaf.oakclient.event.events.render.RenderLabelIfPresentEvent;
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
    @Shadow protected abstract boolean hasLabel(T entity);

    @Final
    @Shadow protected EntityRenderDispatcher dispatcher;

    @Inject(method = "renderLabelIfPresent", at=@At("TAIL"))
    private void onRenderLabelIfPresent(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
        if(entity.isAlive()){
            EventBus.getEventBus().post(new RenderLabelIfPresentEvent(text, (LivingEntity) entity));
        }
    }




    @Inject(method = "render", at=@At("HEAD"))
    void onEntityLabelRender(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
        EventBus.getEventBus().post(new RenderEntityLabelEvent(entity, matrices, light, vertexConsumers, dispatcher));
    }

    //TODO ugly code, make this two methods below better

//    @Redirect(method = "render", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;hasLabel(Lnet/minecraft/entity/Entity;)Z"))
//    boolean hasLabel(EntityRenderer instance, T entity){
//        EntityOwner entityOwner = null;
//        if((entityOwner = (EntityOwner) Category.getModule(EntityOwner.class)) != null && entityOwner.isEnabled().isOn()){
//            String ownerUsername = entityOwner.entityToOwnerUsername.get(entity);
//            return hasLabel(entity) || (ownerUsername != null && !ownerUsername.equals("Failed to resolve username")); //why this particular string? check EntityOwner
//        }
//        return hasLabel(entity);
//    }

//    @ModifyArgs(method = "render", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
//    void renderLableIfPresent(Args args){
//        EntityOwner entityOwner = null;
//        if((entityOwner = (EntityOwner) Category.getModule(EntityOwner.class)) != null && entityOwner.isEnabled().isOn()){
//            String ownerUsername = entityOwner.entityToOwnerUsername.get(args.get(0));
//            if(ownerUsername!=null){
//                args.set(1, Text.of(ownerUsername));
//            }
//        }
//    }
}
