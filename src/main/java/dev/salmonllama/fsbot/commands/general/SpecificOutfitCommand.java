/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.general;

import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;
import org.javacord.api.entity.channel.ServerTextChannel;
import dev.salmonllama.fsbot.utilities.database.DatabaseUtilities;
import org.javacord.api.entity.channel.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;

public class SpecificOutfitCommand extends Command {
    @Override public String name() { return "Specific Outfit"; }
    @Override public String description() { return "Generates an outfit of a specific tag"; }
    @Override public String usage() { return "<String tag> [int number]"; }
    @Override public String category() { return "General"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.NONE); }
    @Override public Collection<String> aliases() { return new ArrayList<>(db.getTags()); }

    private final DatabaseUtilities db;

    public SpecificOutfitCommand(DatabaseUtilities db) {
        this.db = db;
    }

    @Override
    public void onCommand(CommandContext ctx) {
        String[] args = ctx.getArgs();
        String tag = ctx.getUsedAlias();
        TextChannel channel = ctx.getChannel();

        if (args.length == 1) {
            // Send the specific number of outfits with a max of 5
            int outfitNumber = Integer.parseInt(args[0]);
            if (outfitNumber > 5) { outfitNumber = 5; }

            for (int i = 0; i < outfitNumber; i++) {
                channel.sendMessage(db.randomTaggedOutfit(tag).generateEmbed());
            }
        }
        else if (args.length > 1) {
            channel.sendMessage("You did that wrong, mate");
        }
        else {
            channel.sendMessage(db.randomTaggedOutfit(tag).generateEmbed());
        }
    }
}
