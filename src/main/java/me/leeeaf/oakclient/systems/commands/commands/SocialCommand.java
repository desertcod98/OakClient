package me.leeeaf.oakclient.systems.commands.commands;

import me.leeeaf.oakclient.systems.commands.Command;
import me.leeeaf.oakclient.systems.social.PlayerNotFoundException;
import me.leeeaf.oakclient.systems.social.Relationship;
import me.leeeaf.oakclient.systems.social.SocialManager;
import net.minecraft.text.Text;

import static me.leeeaf.oakclient.OakClientClient.mc;

public class SocialCommand extends Command {
    public SocialCommand() {
        super("social", "manage friends and enemies", new String[]{"social", "relationship", "relationships", "friend", "friends", "enemy", "enemies"}, null);
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 2) {
            showUsageMessage();
        } else {
            Relationship relationship = null;
            switch (args[0].toLowerCase()) {
                case "friend":
                    relationship = Relationship.FRIEND;
                    break;
                case "enemy":
                    relationship = Relationship.ENEMY;
                    break;
                case "remove":
                    break;
                default:
                    showUsageMessage();
                    return;
            }
            try {
                SocialManager.addRelationship(args[1], relationship);
                mc.player.sendMessage(Text.of("Added "+args[1])); //TODO does this work?
            } catch (PlayerNotFoundException e) {
                mc.player.sendMessage(Text.of(e.getMessage()));
            }
        }
    }

    private void showUsageMessage() {
        mc.player.sendMessage(Text.of("Usage: .social [friend/enemy/remove] 'playerName'"));
    }
}
