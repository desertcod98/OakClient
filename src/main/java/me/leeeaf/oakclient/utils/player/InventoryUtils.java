package me.leeeaf.oakclient.utils.player;

import net.minecraft.item.Item;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;

import java.util.function.Predicate;

import static me.leeeaf.oakclient.OakClient.mc;

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

    public static Item getItemOnIndexHotbar(int slot){
        if (slot < 0 || slot > 8) return null;
        return mc.player.getInventory().getStack(slot).getItem();
    }

    public static int getFirstItemHotbar(Predicate<Item> itemPredicate){
        for(int i = 0; i < 9 ; i++){
            if(itemPredicate.test(mc.player.getInventory().getStack(i).getItem())){
                return i;
            }
        }
        return -1;
    }
}
