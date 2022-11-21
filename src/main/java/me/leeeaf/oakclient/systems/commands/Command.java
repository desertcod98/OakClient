package me.leeeaf.oakclient.systems.commands;

public abstract class Command {
    public final String name, description;
    public final String[] triggers;
    protected Command(String name, String description, String[] triggers) {
        this.name = name;
        this.description = description;
        this.triggers = triggers;
    }

    public abstract void excecute(String[] args);
    @Override
    public String toString(){
        return name+" : "+description;
    }

    public String helpMessage(){
        return "No help info for command: "+name;
    }


}
