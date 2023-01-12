package me.leeeaf.oakclient.mixin;

import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.world.XRay;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/NativeImage;setColor(III)V"))
    private void update(Args args) {
        XRay xRay = (XRay) Category.getModule(XRay.class);
        if (xRay != null && xRay.isEnabled().isOn()) args.set(2, 0xFFFFFFFF);
    }
}
