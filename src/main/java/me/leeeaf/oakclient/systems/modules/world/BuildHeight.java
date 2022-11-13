package me.leeeaf.oakclient.systems.modules.world;

import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.IEventListener;
import me.leeeaf.oakclient.event.events.packets.PacketSendEvent;
import me.leeeaf.oakclient.mixin.BlockHitResultAccessor;
import me.leeeaf.oakclient.systems.modules.Category;
import me.leeeaf.oakclient.systems.modules.Module;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.math.Direction;

public class BuildHeight extends Module implements IEventListener {
    public BuildHeight() {
        super("Build height", "Allows you to place blocks over the build limit", ()->true, true, Category.WORLD);
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
    public void onTick() {

    }

    @Override
    public void call(Object event) {
        if ((((PacketSendEvent)event).packet instanceof PlayerInteractBlockC2SPacket packet)){
            if (packet.getBlockHitResult().getPos().y >= 319 && packet.getBlockHitResult().getSide() == Direction.UP) {
                ((BlockHitResultAccessor) packet.getBlockHitResult()).setSide(Direction.DOWN); //todo enters if but doesent work
            }
        }
    }

    @Override
    public Class<?> getTarget() {
        return PacketSendEvent.class;
    }
}
