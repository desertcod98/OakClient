package me.leeeaf.oakclient.mixin;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import me.leeeaf.oakclient.event.EventBus;
import me.leeeaf.oakclient.event.events.PacketEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin {
    @Shadow
    private Channel channel;

    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/Packet;)V", cancellable = true)
    private void onSendPacketHead(Packet<?> packet, CallbackInfo info) {
        if(EventBus.getEventBus().post(new PacketEvent.Send(packet)).isCancelled()) info.cancel();
    }

    @Inject(at=@At("HEAD"), method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", cancellable = true)
    private void onReceivePacketHead(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci){
        if (channel.isOpen() && packet != null) {
            if(EventBus.getEventBus().post(new PacketEvent.Receive(packet)).isCancelled()) ci.cancel();
        }
    }
}
