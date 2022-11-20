package me.leeeaf.oakclient.systems.modules.player;

import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;

public class Portals extends Module {
    public Portals() {
        super("Portals", "Makes you able to use GUIs in portals", ()->true, true, Category.PLAYER);
        //logic handled in ClientPlayerEntityMixin::updateNausea()
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onTick() {

    }
}
