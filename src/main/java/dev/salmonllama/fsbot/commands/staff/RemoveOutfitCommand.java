/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.staff;

import com.vdurmont.emoji.EmojiParser;
import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;
import dev.salmonllama.fsbot.listeners.ReactionDeleteConfirmationListener;
import dev.salmonllama.fsbot.utilities.Outfit;
import dev.salmonllama.fsbot.utilities.database.DatabaseUtilities;
import dev.salmonllama.fsbot.utilities.exceptions.DiscordError;
import dev.salmonllama.fsbot.utilities.exceptions.OutfitNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class RemoveOutfitCommand extends Command {
    @Override public String name() { return "Remove Outfit"; }
    @Override public String description() { return "Removes an outfit from the database given an id"; }
    @Override public String usage() { return "remove <String id>"; }
    @Override public String category() { return "Staff"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.ROLE, BotConfig.STAFF_ROLE); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("removeoutfit", "remove")); }

    private final DatabaseUtilities db;

    public RemoveOutfitCommand(DatabaseUtilities db) {
        this.db = db;
    }

    @Override
    public void onCommand(CommandContext ctx) {
        String[] args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        User author = ctx.getUser();

        if (args.length != 1) {
            channel.sendMessage("You did that wrong, mate");
            return;
        }

        // get the outfit, confirm deletion through reaction.
        try {
            Outfit outfit = db.getOutfitFromId(args[0]);

            channel.sendMessage(outfit.generateInfo().setTitle("Confirm Outfit Deletion")).thenAccept(message -> {
                message.addReaction(EmojiParser.parseToUnicode(":white_check_mark:"));
                message.addReaction(EmojiParser.parseToUnicode(":octagonal_sign:"));
                message.addReactionAddListener(new ReactionDeleteConfirmationListener(author, message, outfit, db));
            });
        }
        catch (OutfitNotFoundException e) {
            channel.sendMessage(new DiscordError(e.getMessage()).get());
        }
    }
}
