package me.leeeaf.oakclient.mixin;


import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.player.Portals;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

    @Shadow public abstract void closeHandledScreen();

    @Shadow @Final protected MinecraftClient client;

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

    @Redirect(method = "updateNausea", at=@At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;"))
    Screen updateNausea(MinecraftClient instance){
        Portals portals = (Portals) Category.PLAYER.getModules().filter(iModule -> iModule instanceof Portals).findFirst().orElse(null);
        if(portals!=null && portals.isEnabled().isOn()){
            return null;
        }else{
            return client.currentScreen;
        }
    }
}
