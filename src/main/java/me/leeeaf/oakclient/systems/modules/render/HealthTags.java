package me.leeeaf.oakclient.systems.modules.render;

import me.leeeaf.oakclient.event.EventListener;
import me.leeeaf.oakclient.event.events.RenderLableIfPresentEvent;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class HealthTags extends Module {
    public HealthTags() {
        super("Health tags", "Shows entities health in nametags", ()->true, true, Category.RENDER);
    }

    @EventListener
    public void onRenderLableIfPresent(RenderLableIfPresentEvent event) {
        LivingEntity entity = event.entity;
        Text text = event.lableText;
        int health = (int) entity.getHealth();
        MutableText formattedHealth = Text.literal(" ")
                .append(Integer.toString(health)).formatted(getColor(health));
        ((MutableText)text).append(formattedHealth);
    }

    private Formatting getColor(int health)
    {
        if(health <= 5)
            return Formatting.DARK_RED;

        if(health <= 10)
            return Formatting.GOLD;

        if(health <= 15)
            return Formatting.YELLOW;

        return Formatting.GREEN;
    }
}
