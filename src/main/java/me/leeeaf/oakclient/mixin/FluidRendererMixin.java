//package me.leeeaf.oakclient.mixin;
//
//import me.leeeaf.oakclient.systems.modules.Category;
//import me.leeeaf.oakclient.systems.modules.world.XRay;
//import net.minecraft.block.BlockState;
//import net.minecraft.client.render.block.FluidRenderer;
//import net.minecraft.fluid.FluidState;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Direction;
//import net.minecraft.world.BlockRenderView;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//@Mixin(FluidRenderer.class)
//public class FluidRendererMixin {
//    static XRay xray;
//    @Inject(method = "shouldRenderSide", at=@At("RETURN"), cancellable = true)
//    private static void shouldRenderSide(BlockRenderView world, BlockPos pos, FluidState fluidState, BlockState blockState, Direction direction, FluidState neighborFluidState, CallbackInfoReturnable<Boolean> cir){
//        if(xray == null){
//            xray = (XRay) Category.WORLD.getModules().filter(iModule -> iModule instanceof XRay).findFirst().orElse(null);
//        }else if(xray.isEnabled().isOn() && !xray.shouldRenderFluids()){
//            cir.setReturnValue(false); //todo doesent work? check if it enters if
//        }
//    }
//}
