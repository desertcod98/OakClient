package me.leeeaf.oakclient.mixin;


import com.mojang.authlib.GameProfile;
import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.events.ClientMoveEvent;
import me.leeeaf.oakclient.event.events.PostTickEvent;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.systems.modules.movement.SafeWalk;
import me.leeeaf.oakclient.systems.modules.player.Freecam;
import me.leeeaf.oakclient.systems.modules.world.Portals;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    @Shadow @Final protected MinecraftClient client;

    @Shadow public abstract boolean isSneaking();

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile, @Nullable PlayerPublicKey publicKey) {
        super(world, profile, publicKey);
    }

    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    void onPushOutOfBlocks(double x, double z, CallbackInfo ci){
        Module freecam =  Category.getModule(Freecam.class);
        if(freecam != null && freecam.isEnabled().isOn()) ci.cancel();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    void postTick(CallbackInfo ci){
        EventBus.getEventBus().post(new PostTickEvent());
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

    @Inject(method = "move", at=@At("HEAD"), cancellable = true)
    void onMove(MovementType movementType, Vec3d movement, CallbackInfo ci){
        if(EventBus.getEventBus().post(new ClientMoveEvent()).isCancelled()){
            ci.cancel();
        }
    }

    @Override
    protected boolean clipAtLedge() {
        SafeWalk safeWalk = (SafeWalk) Category.MOVEMENT.getModules()
                .filter(iModule -> iModule instanceof SafeWalk)
                .findFirst().orElse(null);
        if(safeWalk != null && safeWalk.isEnabled().isOn()) return true;
        return this.isSneaking();

    }
}
