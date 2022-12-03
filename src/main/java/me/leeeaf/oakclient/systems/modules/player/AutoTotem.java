package me.leeeaf.oakclient.systems.modules.player;

import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.IEventListener;
import me.leeeaf.oakclient.event.events.TotemUsedEvent;
import me.leeeaf.oakclient.gui.setting.IntegerSetting;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class AutoTotem extends Module implements IEventListener {
//    private final IntegerSetting delay = new IntegerSetting("Delay", "delay","Time in ticks to wait before putting totem in offhand", ()->true, 0, 2000, 50); todo HOW
    //todo works only in local (maybe dont use event but onTick)

    public AutoTotem() {
        super("AutoTotem", "Place totems in your offhand", ()->true, true, Category.COMBAT);
    }


    @Override
    public void onEnable(){
        EventBus.getEventBus().subscribe(this);
    }

    @Override
    public void onDisable(){
        EventBus.getEventBus().unsubscribe(this);
    }

    @Override
    public void call(Object event) {
        for(int i = 0; i < PlayerInventory.MAIN_SIZE; i++){
            ItemStack itemStack = mc.player.getInventory().getStack(i);
            if(itemStack.getItem().equals(Items.TOTEM_OF_UNDYING)){ //works only offline? todo
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, mc.player); //offhand index == 45 :( why the fuck
            }
        }
    }

    @Override
    public Class<?>[] getTargets() {
        return new Class[]{TotemUsedEvent.class};
    }
}
