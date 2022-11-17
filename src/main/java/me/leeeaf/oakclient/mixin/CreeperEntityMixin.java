package me.leeeaf.oakclient.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreeperEntity.class)
public class CreeperEntityMixin {
    @Inject(method = "getHurtSound",at=@At("RETURN"),cancellable = true)
    void getHurtSoundInject(DamageSource source, CallbackInfoReturnable<SoundEvent> cir){
        cir.setReturnValue(SoundEvents.ENTITY_ENDERMAN_HURT); //todo remove lol
    }
}
