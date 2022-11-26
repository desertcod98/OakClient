package me.leeeaf.oakclient.systems.commands;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {
    public final String name, description;
    public final String[] triggers;
    public final MutableText formattedName;
    protected final List<Command> possibleArgs = new ArrayList<>();
    protected Command(String name, String description, String[] triggers, Command[] possibleArgs) {
        this.name = name;
        this.description = description;
        this.triggers = triggers;
        formattedName = Text.literal(name).formatted(Formatting.GREEN);
        if(possibleArgs!=null){
            this.possibleArgs.addAll(List.of(possibleArgs));
        }
    }

    public abstract void excecute(String[] args);
    @Override
    public String toString(){
        return name+" : "+description;
    }

    public Text toText(){
        return Text.empty().append(formattedName).append(" : ").append(description);
    }

    public Text helpMessage(int prefixLength){
        String prefix = "-".repeat(Math.max(0, prefixLength));
        MutableText helpMessage = Text.literal(prefix).append(toText().copy());
        if(possibleArgs.size()>0){
            for(Command arg : possibleArgs){
                helpMessage.append("\n").append(prefix).append(arg.helpMessage(prefixLength+1)); //todo wrong, shows too many "-" :(
            }
        }
        return helpMessage;
    }


}
