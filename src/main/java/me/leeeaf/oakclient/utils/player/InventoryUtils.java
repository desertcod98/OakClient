package me.leeeaf.oakclient.utils.player;

import net.minecraft.item.Item;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class InventoryUtils {
    public static int getItemSlotInHotbar(Item item){
        for(int i = 0; i < 9 ; i++){
            if(mc.player.getInventory().getStack(i).getItem() == item){
                return i;
            }
        }
        return -1;
    }

    public static boolean selectSlot(int slot){
        if (slot < 0 || slot > 8) return false;

        mc.player.getInventory().selectedSlot = slot;
        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(slot)); //needed?
        return true;
    }

}
