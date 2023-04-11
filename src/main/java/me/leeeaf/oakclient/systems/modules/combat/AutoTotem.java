package me.leeeaf.oakclient.systems.modules.combat;

import me.leeeaf.oakclient.gui.setting.BooleanSetting;
import me.leeeaf.oakclient.gui.setting.IntegerSetting;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.utils.player.InventoryUtils;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

import static me.leeeaf.oakclient.OakClient.mc;

public class AutoTotem extends Module {
    private BooleanSetting lowHealthTrigger = new BooleanSetting("Low health", "lowHealth", "Places totem in offhand when health is low", () -> true, true);
    private IntegerSetting lowHealthAmount = new IntegerSetting("Amount", "lowHealthAmount", "How low should health be to trigger", () -> true, 1, 20, 6);

    public AutoTotem() {
        super("AutoTotem", "Place totems in your offhand", () -> true, true, Category.COMBAT);
        settings.add(lowHealthTrigger);
        lowHealthTrigger.subSettings.add(lowHealthAmount);
    }


    //TODO add delay
    @Override
    public void onTick() {
        if(mc.player.getInventory().offHand.get(0).getItem().equals(Items.TOTEM_OF_UNDYING)) return;
        if (lowHealthTrigger.isOn() && mc.player.getHealth() <= lowHealthAmount.getValue()) {
            placeTotemInOffhand();
        }
    }

    private void placeTotemInOffhand() {
        boolean itemInOffhand = !mc.player.getOffHandStack().isEmpty();
        for (int i = 0; i < PlayerInventory.MAIN_SIZE; i++) {
            ItemStack itemStack = mc.player.getInventory().getStack(i);
            if (itemStack.getItem().equals(Items.TOTEM_OF_UNDYING)) {
                if(i <= 9) { //if it is in hotbar
                    int selectedSlot = mc.player.getInventory().selectedSlot;
                    InventoryUtils.selectSlot(i);
                    InventoryUtils.swapOffhand();
                    InventoryUtils.selectSlot(selectedSlot);
                }else{
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, mc.player); //offhand index == 45 :( why the fuck
                    if (itemInOffhand) {
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
                    }
                }

            }
        }
    }


}
