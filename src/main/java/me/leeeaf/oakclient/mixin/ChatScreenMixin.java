package me.leeeaf.oakclient.mixin;

import me.leeeaf.oakclient.systems.commands.Command;
import me.leeeaf.oakclient.systems.commands.CommandRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Arrays;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    private final String PREFIX = ".";

    @Redirect(
            method = "sendMessage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendChatMessage(Ljava/lang/String;Lnet/minecraft/text/Text;)V")
    )
    void redirectSendChatMessage(ClientPlayerEntity instance,String message, Text preview){
        if(message.startsWith(PREFIX)){
            String trimmedMessage = message.substring(PREFIX.length());
            if(trimmedMessage.isEmpty() || trimmedMessage.isBlank()) return;
            String[] messageSplit = trimmedMessage.trim().split(" +"); //splits message when it finds 1 or more spaces
            String command = messageSplit[0];
            String[] args = Arrays.copyOfRange(messageSplit, 1,messageSplit.length);

            Command toRun = CommandRegistry.getByAlias(command);
            if(toRun == null){
                MinecraftClient.getInstance().player.sendMessage(Text.of("Command not found"));
            }else{
                toRun.excecute(args);
            }
        }else{
            instance.sendChatMessage(message, preview);
        }
    }
}
