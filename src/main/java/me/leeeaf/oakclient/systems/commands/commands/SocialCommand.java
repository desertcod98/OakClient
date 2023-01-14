package me.leeeaf.oakclient.systems.commands.commands;

import me.leeeaf.oakclient.systems.commands.Command;
import me.leeeaf.oakclient.systems.social.PlayerNotFoundException;
import me.leeeaf.oakclient.systems.social.Relationship;
import me.leeeaf.oakclient.systems.social.SocialManager;
import me.leeeaf.oakclient.utils.io.ChatLogger;
import net.minecraft.text.Text;


public class SocialCommand extends Command {
    //TODO add a command to show relationships
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
                    try{
                        SocialManager.removeRelationship(args[1]);
                    } catch (PlayerNotFoundException e) {
                        ChatLogger.error(Text.of(e.getMessage()));
                    }
                    break;
                default:
                    showUsageMessage();
                    return;
            }
            try {
                SocialManager.addRelationship(args[1], relationship);
                Text formattedRelationshipText = Text.literal(relationship.toString()).formatted(relationship.getColor());
                ChatLogger.log((Text.literal(args[1] + " is now your ").append(formattedRelationshipText)));
            } catch (PlayerNotFoundException e) {
                ChatLogger.error(Text.of(e.getMessage()));
            }
        }
    }

    private void showUsageMessage() {
        ChatLogger.error(Text.of(".social usage: .social [friend/enemy/remove] 'playerName'"));
    }
}
