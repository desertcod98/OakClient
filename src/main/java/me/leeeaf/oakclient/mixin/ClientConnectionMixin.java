package me.leeeaf.oakclient.mixin;

import me.leeeaf.oakclient.OakClientClient;
import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.events.packets.PacketSendEvent;
import net.minecraft.client.network.ChatPreviewer;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    private ChatPreviewer chatPreviewer = new ChatPreviewer(OakClientClient.mc);

    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/Packet;)V", cancellable = true)
    private void onSendPacketHead(Packet<?> packet, CallbackInfo info) {
        if(EventBus.getEventBus().post(new PacketSendEvent(packet)).isCancelled()) info.cancel();
    }
}
