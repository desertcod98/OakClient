package me.leeeaf.oakclient.systems.modules.world;

import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;

public class Portals extends Module {
    public Portals() {
        super("Portals", "Makes you able to use GUIs in portals", ()->true, true, Category.WORLD);
        //logic handled in ClientPlayerEntityMixin::updateNausea()
    }
}
