package me.leeeaf.oakclient.systems.modules.combat;

import me.leeeaf.oakclient.gui.setting.BooleanSetting;
import me.leeeaf.oakclient.gui.setting.IntegerSetting;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.utils.io.ChatLogger;
import me.leeeaf.oakclient.utils.player.InventoryUtils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.MathHelper;

import static me.leeeaf.oakclient.OakClient.mc;

public class AutoTotem extends Module {
    private BooleanSetting lowHealthTrigger = new BooleanSetting("Low health", "lowHealth", "Places totem in offhand when health is low", () -> true, true);
    private IntegerSetting lowHealthAmount = new IntegerSetting("Amount", "lowHealthAmount", "How low should health be to trigger", () -> true, 1, 20, 6);
    private BooleanSetting dangerousFallTrigger = new BooleanSetting("Dangerous fall", "dangerousFall", "Places totem in offhand when the fall could kill you", () -> true, true);

    public AutoTotem() {
        super("AutoTotem", "Place totems in your offhand", () -> true, true, Category.COMBAT);
        settings.add(lowHealthTrigger);
        lowHealthTrigger.subSettings.add(lowHealthAmount);
        settings.add(dangerousFallTrigger);
    }


    //TODO add delay
    @Override
    public void onTick() {
        if(mc.player.getInventory().offHand.get(0).getItem().equals(Items.TOTEM_OF_UNDYING)) return;
        if (lowHealthTrigger.isOn() && mc.player.getHealth() <= lowHealthAmount.getValue()) {
            placeTotemInOffhand();
            return;
        }
        if(dangerousFallTrigger.isOn() && mc.player.getHealth() - computeFallDamage() <= 6){
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
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, mc.player); //offhand index == 45 :( why the fuc
                    if (itemInOffhand) {
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
                    }
                }
            }
        }
    }

    private int computeFallDamage() {
        StatusEffectInstance statusEffectInstance = mc.player.getStatusEffect(StatusEffects.JUMP_BOOST);
        float f = statusEffectInstance == null ? 0.0f : (float)(statusEffectInstance.getAmplifier() + 1);
        return MathHelper.ceil(mc.player.fallDistance - 3.0f - f);
    }

}
