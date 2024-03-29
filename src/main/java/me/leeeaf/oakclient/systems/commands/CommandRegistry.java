package me.leeeaf.oakclient.systems.commands;

import me.leeeaf.oakclient.systems.commands.commands.*;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandRegistry {
    private static final List<Command> commands = Util.make(new ArrayList<>(), CommandRegistry::initCommands);

    private static void initCommands(List<Command> commands){
        commands.add(new FOVCommand());
        commands.add(new HelpCommand());
        commands.add(new SearchCommand());
        commands.add(new ReloadSoundsCommand());
        commands.add(new ReloadCommand());
        commands.add(new XrayCommand());
        commands.add(new SocialCommand());
    }

    public static List<Command> getCommands(){
        return commands;
    }
    public static Command getByAlias(String commandAlias){
        for(Command command : getCommands()){
            if(Arrays.stream(command.triggers).anyMatch(s -> s.equalsIgnoreCase(commandAlias))){
                return command;
            }
        }
        return null;
    }
}
