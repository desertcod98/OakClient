package me.leeeaf.oakclient.event.events.render;

import me.leeeaf.oakclient.event.Event;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

public class RenderLabelIfPresentEvent extends Event {
    public Text lableText;
    public LivingEntity entity;

    public RenderLabelIfPresentEvent(Text lableText, LivingEntity entity) {
        this.lableText = lableText;
        this.entity = entity;
    }
}
