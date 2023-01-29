package me.leeeaf.oakclient.systems.modules.player;

import me.leeeaf.oakclient.gui.setting.IntegerSetting;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.utils.Timer;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import static me.leeeaf.oakclient.OakClient.mc;

public class ChestStealer extends Module {
    private Timer delayTimer = new Timer();
    private final IntegerSetting delay = new IntegerSetting("Delay", "Delay", "Milliseconds between each 'click'", () -> true, 0, 1000, 100);

    //TODO implement autoclose
    public ChestStealer() {
        super("ChestStealer", "Gets items from chests", ()->true, true, Category.PLAYER);
        settings.add(delay);
    }


    @Override
    public void onTick() {
        if (mc.currentScreen instanceof GenericContainerScreen) {
            ScreenHandler handler = mc.player.currentScreenHandler;

            for (int i = 0; i < handler.slots.size() - 35 ; i++) {//35???? (was InventoryUtils.MAIN_HAND)
                Slot slot = handler.slots.get(i);
                ItemStack stack = slot.getStack();
                if (stack.getItem() != Items.AIR) {
                    if (delayTimer.hasTimeElapsed(delay.getValue(), true)) {
                        mc.interactionManager.clickSlot(handler.syncId, slot.id, 0, SlotActionType.QUICK_MOVE, mc.player);
                    }
                }
            }
        }
    }
}
