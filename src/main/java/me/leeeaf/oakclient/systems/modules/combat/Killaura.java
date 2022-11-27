package me.leeeaf.oakclient.systems.modules.combat;

import me.leeeaf.oakclient.gui.setting.BooleanSetting;
import me.leeeaf.oakclient.gui.setting.DoubleSetting;
import me.leeeaf.oakclient.gui.setting.EnumSetting;
import me.leeeaf.oakclient.gui.setting.KeybindSetting;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import me.leeeaf.oakclient.utils.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class Killaura extends Module {
    private final DoubleSetting range = new DoubleSetting("Range", "Range", "Maximum range of attacksTEST", ()->true, 0, 6, 4.25);
    private final BooleanSetting delay1_9 = new BooleanSetting("1.9 delay", "1.9 delay", "Should use 1.9 delay?", ()->true, false);
    private final BooleanSetting attackThroughBlocks = new BooleanSetting("Through blocks", "Through blocks", "Should attack through blocks?", ()->true, false);
    private final EnumSetting<SortMethod> sortMethod = new EnumSetting<>("Sort method", "SortMethod", "How to sort entities", () -> true, SortMethod.HEALTH, SortMethod.class);
    public final KeybindSetting keybind=new KeybindSetting("Keybind","keybind","The key to toggle the module.",()->true, GLFW.GLFW_KEY_DELETE);

    public Killaura() {
        super("Killaura", "Attacks entities around the player", ()->true, true, Category.COMBAT);
        settings.add(range);
        settings.add(delay1_9);
        settings.add(attackThroughBlocks);
        settings.add(keybind);
        settings.add(sortMethod);
    }

    @Override
    public void onTick() {
        if(!mc.player.isAlive()){
            return;
        }
        boolean cooldownDone = !delay1_9.isOn() || mc.player.getAttackCooldownProgress(mc.getTickDelta()) == 1.0f;

        if(cooldownDone){
            for (Entity entity : getEntities()){
                boolean wasSprinting = mc.player.isSprinting();
                if (wasSprinting)
                    mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));

                mc.interactionManager.attackEntity(mc.player, entity);
                mc.player.swingHand(Hand.MAIN_HAND);
                if (wasSprinting)
                    mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
            }
        }


    }

    private List<Entity> getEntities() {
        Stream<Entity> targets = StreamSupport.stream(mc.world.getEntities().spliterator(), false);
        Comparator<Entity> comparator = null;
        switch(sortMethod.getValue()){
            case HEALTH -> comparator = Comparator.comparing((entity)-> ((LivingEntity) entity).getHealth());
            case DISTANCE -> comparator = Comparator.comparing(mc.player::distanceTo);
        }
        return targets
                .filter(entity -> EntityUtils.isAttackable(entity) && (attackThroughBlocks.isOn() || mc.player.canSee(entity)) && mc.player.distanceTo(entity) <= range.getValue())
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public enum SortMethod{
        DISTANCE,
        HEALTH
    }
}
