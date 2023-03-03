package me.leeeaf.oakclient.mixin;

import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.render.AntiWeather;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin {
    AntiWeather antiWeather;

    @Inject(method = "getRainGradient", at = @At("RETURN"), cancellable = true)
    void getRainGradient(float delta, CallbackInfoReturnable<Float> cir) {
        antiWeather = (AntiWeather) Category.getModule(AntiWeather.class);
        if (antiWeather != null && antiWeather.isEnabled().isOn()) {
            cir.setReturnValue(0f);
        }
    }

    @Inject(method = "getThunderGradient", at = @At("RETURN"), cancellable = true)
    void getThunderGradient(float delta, CallbackInfoReturnable<Float> cir) {
        antiWeather = (AntiWeather) Category.getModule(AntiWeather.class);
        if (antiWeather != null && antiWeather.isEnabled().isOn()) {
            cir.setReturnValue(0f);
        }
    }
}
