package me.leeeaf.oakclient.event.events;

import me.leeeaf.oakclient.event.Cancellable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

public class RenderLableIfPresentEvent extends Cancellable {
    public Text lableText;
    public LivingEntity entity;

    public RenderLableIfPresentEvent(Text lableText, LivingEntity entity) {
        this.lableText = lableText;
        this.entity = entity;
    }
}
