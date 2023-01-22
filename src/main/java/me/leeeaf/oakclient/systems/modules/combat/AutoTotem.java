package me.leeeaf.oakclient.systems.modules.combat;

import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

import static me.leeeaf.oakclient.OakClient.mc;

public class AutoTotem extends Module {


    public AutoTotem() {
        super("AutoTotem", "Place totems in your offhand", ()->true, true, Category.COMBAT);
    }

    //TODO make this useful
    @Override
    public void onTick() {
        if(!mc.player.getInventory().offHand.get(0).getItem().equals(Items.TOTEM_OF_UNDYING)){
            boolean itemInOffhand = !mc.player.getOffHandStack().isEmpty();
            for(int i = 0; i < PlayerInventory.MAIN_SIZE; i++){
                ItemStack itemStack = mc.player.getInventory().getStack(i);
                if(itemStack.getItem().equals(Items.TOTEM_OF_UNDYING)){
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, mc.player); //offhand index == 45 :( why the fuck
                    if(itemInOffhand){
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
                    }
                }
            }
        }
    }
}
