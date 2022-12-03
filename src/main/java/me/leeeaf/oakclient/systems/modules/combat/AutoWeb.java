package me.leeeaf.oakclient.systems.modules.combat;

import me.leeeaf.oakclient.gui.setting.BooleanSetting;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.utils.EntityUtils;
import me.leeeaf.oakclient.utils.player.InventoryUtils;
import me.leeeaf.oakclient.utils.world.BlockUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class AutoWeb extends Module {
    private final BooleanSetting swapBack = new BooleanSetting("Swap back", "swapBack", "Swap back to previous item", () -> true, true);
    private final BooleanSetting airPlace = new BooleanSetting("Air place", "airPlace", "Places blocks in air", () -> true, false);

    public AutoWeb() {
        super("AutoWeb", "Places webs on players", () -> true, true, Category.COMBAT);
        settings.add(swapBack);
    }

    @Override
    public void onTick() {
        for (PlayerEntity player : EntityUtils.getPlayers(5, EntityUtils.SortMethod.DISTANCE)) {
            int webSlot;
            if ((webSlot = InventoryUtils.getItemSlotInHotbar(Items.COBWEB)) != -1) {
                if (BlockUtils.canPlace(player.getBlockPos(), false)) {
                    int prevSlot = mc.player.getInventory().selectedSlot;
                    InventoryUtils.selectSlot(webSlot);
                    BlockUtils.place(player.getBlockPos(), webSlot, Hand.MAIN_HAND, true, true, true, airPlace.getValue());
                    if (swapBack.getValue()) InventoryUtils.selectSlot(prevSlot);
                }
            }
        }
    }
}

