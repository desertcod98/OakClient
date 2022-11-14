package me.leeeaf.oakclient.systems.modules.world;

import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class Fullbright extends Module {
    public Fullbright() {
        super("Fullbright", "Makes you a cat", ()->true, true, Category.WORLD);
    }

    @Override
    public void onDisable() {
        mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onTick() {
        mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 500, 2));
    }
}
