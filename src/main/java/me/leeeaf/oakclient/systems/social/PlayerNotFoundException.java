package me.leeeaf.oakclient.systems.social;

public class PlayerNotFoundException extends Exception{
    public PlayerNotFoundException(String player){
        super("Player "+player+" not found!");
    }
}
