package me.leeeaf.oakclient.systems.modules.world;

import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;

public class AntiCactus extends Module {
    public AntiCactus() {
        super("Anti Cactus", "Tries to prevent the player from taking cactus damage", ()->true, true, Category.WORLD);
        //LOGIC HANDLED IN: CactusBlockMixin
    }
}
