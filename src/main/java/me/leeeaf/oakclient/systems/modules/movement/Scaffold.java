package me.leeeaf.oakclient.systems.modules.movement;

import me.leeeaf.oakclient.gui.setting.BooleanSetting;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.utils.world.BlockUtils;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class Scaffold extends Module {
    private final BooleanSetting airPlace = new BooleanSetting("AirPlace", "airPlace", "Allows placing blocks in air", ()->true, false);

    public Scaffold() {
        super("Scaffold", "Places blocks under your feet", ()->true, true, Category.MOVEMENT);
        settings.add(airPlace);
    }

    @Override
    public void onTick() {
        BlockPos placePos = new BlockPos(mc.player.getPos().add(mc.player.getVelocity()).add(0,-0.5,0));
        if(mc.world.getBlockState(placePos).isAir()){
            if(airPlace.isOn()){
                BlockUtils.place(placePos,0, Hand.MAIN_HAND, true, false,true, true);
            }else if(BlockUtils.getPlaceSide(placePos)!=null){
                BlockUtils.place(placePos,0, Hand.MAIN_HAND, true, false,true, false);
            }else{
                for(Direction direction: Direction.values()){
                    if(BlockUtils.getPlaceSide(placePos.offset(direction))!=null){
                        BlockUtils.place(placePos.offset(direction),mc.player.getInventory().selectedSlot, Hand.MAIN_HAND, true, false,true, false);
                        BlockUtils.place(placePos,mc.player.getInventory().selectedSlot, Hand.MAIN_HAND, true, false,true, false);
                        break;
                    }
                }
            }
        }
    }
}
