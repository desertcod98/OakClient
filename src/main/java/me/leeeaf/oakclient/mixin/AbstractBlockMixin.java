package me.leeeaf.oakclient.mixin;

import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.world.XRay;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
    XRay xRay;
    @Inject(method = "getAmbientOcclusionLightLevel", at=@At("HEAD"), cancellable = true)
    void getAmbientOcclusionLevel(BlockState state, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir){
        if(xRay==null){
            xRay = (XRay) Category.WORLD.getModules()
                    .filter(iModule -> iModule instanceof XRay)
                    .findFirst()
                    .orElse(null);
        }else if(xRay.isEnabled().isOn()) cir.setReturnValue(1f);
    }
}
