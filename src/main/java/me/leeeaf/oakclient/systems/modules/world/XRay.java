package me.leeeaf.oakclient.systems.modules.world;

import me.leeeaf.oakclient.gui.setting.BooleanSetting;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.systems.modules.Category;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.ArrayList;
import java.util.Arrays;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class XRay extends Module {
    private final BooleanSetting shouldRenderFluids = new BooleanSetting("Render fluids", "renderFluids", "Should render fluids?", ()->true, true);

    private ArrayList<Block> blocksToRender = new ArrayList<>(Arrays.asList(
            Blocks.DIAMOND_ORE,
            Blocks.COAL_ORE,
            Blocks.LAPIS_ORE
    ));

    public XRay() {
        super("XRay", "Only renders certain blocks", ()->true, true, Category.WORLD);
        settings.add(shouldRenderFluids); //todo
    }

    @Override
    public void onDisable() {
        mc.chunkCullingEnabled = true;
        mc.worldRenderer.reload();
    }

    @Override
    public void onEnable() {
        mc.chunkCullingEnabled = false;
        mc.worldRenderer.reload();
    }


    @Override
    public void onTick() {
    }

    public boolean shouldRenderSide(Block block ){
        return(blocksToRender.contains(block));
    }

    public boolean shouldRenderFluids(){
        return shouldRenderFluids.getValue();
    }
}
