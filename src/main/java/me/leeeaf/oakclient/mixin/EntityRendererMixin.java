package me.leeeaf.oakclient.mixin;

import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.events.RenderLableIfPresentEvent;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.world.EntityOwner;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin <T extends Entity>{
    @Inject(method = "renderLabelIfPresent", at=@At("TAIL"))
    private void onRenderLabelIfPresent(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
        if(entity.isAlive()){
            EventBus.getEventBus().post(new RenderLableIfPresentEvent(text, (LivingEntity) entity));
        }
    }

    //TODO ugly code, make this two methods below better
    @Redirect(method = "render", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;hasLabel(Lnet/minecraft/entity/Entity;)Z"))
    boolean hasLable(EntityRenderer instance, T entity){
        EntityOwner entityOwner = null;
        if((entityOwner = (EntityOwner) Category.getModule(EntityOwner.class)) != null && entityOwner.isEnabled().isOn()){
            String ownerUsername = entityOwner.entityToOwnerUsername.get(entity);
            return ownerUsername != null && !ownerUsername.equals("Failed to resolve username"); //why this particular string? check EntityOwner
        }
        return false;
    }

    @ModifyArgs(method = "render", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    void renderLableIfPresent(Args args){
        EntityOwner entityOwner = null;
        if((entityOwner = (EntityOwner) Category.getModule(EntityOwner.class)) != null && entityOwner.isEnabled().isOn()){
            String ownerUsername = entityOwner.entityToOwnerUsername.get(args.get(0));
            if(ownerUsername!=null){
                args.set(1, Text.of(ownerUsername));
            }
        }
    }
}
