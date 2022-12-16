package me.leeeaf.oakclient.systems.modules.world;

import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;

public class AntiWeather extends Module {
    public AntiWeather() {
        super("Anti weather", "Removes rain, snowing, and thunder.", ()->true, true, Category.WORLD);
        //logic handled in WorldMixin::isRaining, WorldMixin::isThundering
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
