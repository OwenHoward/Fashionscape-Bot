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
import dev.salmonllama.fsbot.listeners.ReactionRetagConfirmationListener;
import dev.salmonllama.fsbot.utilities.Outfit;
import dev.salmonllama.fsbot.utilities.database.DatabaseUtilities;
import dev.salmonllama.fsbot.utilities.exceptions.DiscordError;
import dev.salmonllama.fsbot.utilities.exceptions.OutfitNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class RetagCommand extends Command {
    @Override public String name() { return "Retag"; }
    @Override public String description() { return "Changes the tag of the given outfit"; }
    @Override public String usage() { return "retag <String id> <String newtag>"; }
    @Override public String category() { return "Staff"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.ROLE, BotConfig.STAFF_ROLE); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Collections.singletonList("retag")); }

    private final DatabaseUtilities db;

    public RetagCommand(DatabaseUtilities db) {
        this.db = db;
    }

    @Override
    public void onCommand(CommandContext ctx) {
        String[] args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        User author = ctx.getUser();

        if (args.length != 2) {
            channel.sendMessage("You did that wrong mate. check the help command.");
            return;
        }

        try {
            Outfit outfit = this.db.getOutfitFromId(args[0]);

            channel.sendMessage(outfit.generateInfo().setTitle(String.format("Update tag to %s?", args[1]))).thenAcceptAsync(message -> {
                message.addReaction(EmojiParser.parseToUnicode(":white_check_mark:"));
                message.addReaction(EmojiParser.parseToUnicode(":octagonal_sign:"));
                message.addReactionAddListener(new ReactionRetagConfirmationListener(author, message, outfit, db, outfit.getTag(), args[1]));
            });
        }
        catch (OutfitNotFoundException e) {
            channel.sendMessage(new DiscordError(e.getMessage()).get());
        }
    }
}
