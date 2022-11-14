package me.leeeaf.oakclient.mixin;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import me.leeeaf.oakclient.OakClientClient;
import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.events.packets.PacketRecieveEvent;
import me.leeeaf.oakclient.event.events.packets.PacketSendEvent;
import net.minecraft.client.network.ChatPreviewer;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Shadow
    private Channel channel;

    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/Packet;)V", cancellable = true)
    private void onSendPacketHead(Packet<?> packet, CallbackInfo info) {
        if(EventBus.getEventBus().post(new PacketSendEvent(packet)).isCancelled()) info.cancel();
    }

    @Inject(at=@At("HEAD"), method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", cancellable = true)
    private void onRecievePacketHead(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci){
        if (channel.isOpen() && packet != null) {
            if(EventBus.getEventBus().post(new PacketRecieveEvent(packet)).isCancelled()) ci.cancel();
        }
    }
}
