package me.leeeaf.oakclient.systems.commands.commands;

import me.leeeaf.oakclient.systems.commands.Command;
import me.leeeaf.oakclient.systems.commands.CommandRegistry;
import net.minecraft.text.Text;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "Shows help text", new String[]{"help","Help","?"}, null);
    }

    @Override
    public void execute(String[] args) {
        Command helpCommand;
        if(args.length >0 ){
            if(args[0].equalsIgnoreCase("commands")){
                for(Command command : CommandRegistry.getCommands()){
                    mc.player.sendMessage(command.toText());
                }
            }else if((helpCommand = (CommandRegistry.getCommands().stream() //checks if args[0] is the name of a command, and if it is, it shows the commands help message
                    .filter(command -> command.name.equalsIgnoreCase(args[0]))
                    .findFirst().orElse(null)))!=null){
                mc.player.sendMessage(helpCommand.helpMessage(0));
            }
        }else{
            mc.player.sendMessage(Text.of("Use .help commands to get a list of commands\nUse .help command_name to get a help message about a command"));
        }
    }
}
