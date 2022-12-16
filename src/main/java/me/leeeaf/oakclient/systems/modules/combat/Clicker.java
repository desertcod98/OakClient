package me.leeeaf.oakclient.systems.modules.combat;

import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class Clicker extends Module {
    public Clicker() {
        super("Clicker", "clicks", ()->true, true, Category.COMBAT);
    }

    @Override
    public void onTick() {
        HitResult rayTrace = mc.crosshairTarget;
        if (rayTrace instanceof EntityHitResult && mc.interactionManager != null) {
            mc.interactionManager.attackEntity(mc.player, ((EntityHitResult) rayTrace).getEntity());
        }
        mc.player.swingHand(Hand.MAIN_HAND);
    }

}
