package me.leeeaf.oakclient.utils.io;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static me.leeeaf.oakclient.OakClient.mc;

public class ChatLogger {
    private static final Text logPrefix = Text.literal("[").append(Text.literal("Oak").formatted(Formatting.DARK_GREEN)).append(Text.literal("] "));
    private static final Text errorPrefix = Text.literal("[").append(Text.literal("Oak").formatted(Formatting.RED)).append(Text.literal("] "));


    public static void log(Text message){
        mc.player.sendMessage(logPrefix.copy().append(message));
    }

    public static void error(Text message){
        mc.player.sendMessage(errorPrefix.copy().append(message.copy().formatted(Formatting.RED)));
    }

    public static void error(String message){
        mc.player.sendMessage(errorPrefix.copy().append(Text.literal(message).formatted(Formatting.RED)));
    }

    public static void log(String message){
        mc.player.sendMessage(logPrefix.copy().append(Text.literal(message)));
    }
}
