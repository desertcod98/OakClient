package me.leeeaf.oakclient.systems.commands.commands;

import me.leeeaf.oakclient.systems.commands.Command;
import net.minecraft.text.Text;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class TracersCommand extends Command {
    public TracersCommand() {
        super("tracers", "Manages tracers targets", new String[]{"tracers", "tracer"});
    }

    @Override
    public void excecute(String[] args) {
        if(args.length>0){
            switch (args[0].toLowerCase()){
                case "list":
                    mc.player.sendMessage(Text.of("test"));
            }
        }
    }
    @Override
    public String helpMessage(){
        return """
                -tracers list : show possible targets (and if they are already selected)
                -tracers add : adds target\s
                -tracers remove : removes target""";
    }
}
