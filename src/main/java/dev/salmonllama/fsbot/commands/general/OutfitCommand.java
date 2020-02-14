/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.general;

import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import dev.salmonllama.fsbot.utilities.database.DatabaseUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class OutfitCommand extends Command {
    @Override public String name() { return "Outfit"; }
    @Override public String description() { return "Generates a random image with the given tag. Use ~tags to see valid tags."; }
    @Override public String usage() { return "outfit <String tag>"; }
    @Override public String category() { return "General"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.NONE); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("outfit", "o")); }

    private final DatabaseUtilities db;

    public OutfitCommand(DatabaseUtilities db) {
        this.db = db;
    }

    @Override
    public void onCommand(CommandContext ctx) {
        String[] args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        Message message = ctx.getMessage();
        // Find the first arg for tag/submitter/naught, find the next arg for number to send

        int MAX_OUTFITS = 5;

        if (args.length == 0) {
            channel.sendMessage(db.randomOutfit().generateEmbed());
        }
        else if (message.getMentionedUsers().size() > 0) {
            // Outfit needs to be from submitter
            String userId = message.getMentionedUsers().get(0).getIdAsString();

            if (args.length == 2) {
                // Send a number of outfits
                int iterator = ((Integer.parseInt(args[1]) > MAX_OUTFITS) ? MAX_OUTFITS : Integer.parseInt(args[1]));

                for (int i = 0; i < iterator; i++) {
                    channel.sendMessage(db.getOutfitBySubmitter(userId).generateEmbed());
                }
            }
            else {
                channel.sendMessage(db.getOutfitBySubmitter(userId).generateEmbed());
            }
        }
        else if (db.getTags().contains(args[0].toLowerCase())) {
            // Outfit needs to be tagged
            String tag = args[0];

            if (args.length == 2) {
                // Send a number of outfits
                int iterator = ((Integer.parseInt(args[1]) > MAX_OUTFITS) ? MAX_OUTFITS : Integer.parseInt(args[1]));

                for (int i = 0; i < iterator; i++) {
                    channel.sendMessage(db.randomTaggedOutfit(tag).generateEmbed());
                }
            }
            else {
                channel.sendMessage(db.randomTaggedOutfit(tag).generateEmbed());
            }
        }
        else if (args.length == 1 && args[0].matches("\\d")) {
            int iterator = ((Integer.parseInt(args[0]) > MAX_OUTFITS) ? MAX_OUTFITS : Integer.parseInt(args[0]));

            for (int i = 0; i < iterator; i++) {
                channel.sendMessage(db.randomOutfit().generateEmbed());
            }
        }
        else {
            channel.sendMessage("Something went wrong");
        }
    }
}
