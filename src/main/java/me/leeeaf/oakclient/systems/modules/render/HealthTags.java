package me.leeeaf.oakclient.systems.modules.render;

import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.IEventListener;
import me.leeeaf.oakclient.event.events.RenderLableIfPresentEvent;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class HealthTags extends Module implements IEventListener {
    public HealthTags() {
        super("Health tags", "Shows entities health in nametags", ()->true, true, Category.RENDER);
    }

    @Override
    public void onDisable() {
        EventBus.getEventBus().unsubscribe(this);
    }

    @Override
    public void onEnable() {
        EventBus.getEventBus().subscribe(this);
    }

    @Override
    public void call(Object event) {
        LivingEntity entity = ((RenderLableIfPresentEvent) event).entity;
        Text text = ((RenderLableIfPresentEvent) event).lableText;
        int health = (int) entity.getHealth();
        MutableText formattedHealth = Text.literal(" ")
                .append(Integer.toString(health)).formatted(getColor(health));
        ((MutableText)text).append(formattedHealth);
    }

    @Override
    public Class<?>[] getTargets() {
        return new Class[]{RenderLableIfPresentEvent.class};
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
