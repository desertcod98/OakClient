package me.leeeaf.oakclient.mixin;


import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.systems.modules.Category;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    void postTick(CallbackInfo ci){
        Category.getClient().getCategories().forEach(category -> {
            category.getModules().forEach(module->{
                if(module.isEnabled() != null && module.isEnabled().isOn()){
                    ((Module) module).onTick();
                }
            });
        });
    }
}
