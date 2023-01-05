package me.leeeaf.oakclient.mixin;
import me.leeeaf.oakclient.OakClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {

    @Redirect(method = "init", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/resource/SplashTextResourceSupplier;get()Ljava/lang/String;"))
    String modifySplashText(SplashTextResourceSupplier instance){
        return OakClient.splashText;
    }
}
