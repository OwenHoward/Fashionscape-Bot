/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.staff;

import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;
import org.javacord.api.DiscordApi;
import dev.salmonllama.fsbot.utilities.database.DatabaseUtilities;
import dev.salmonllama.fsbot.utilities.exceptions.DiscordError;
import dev.salmonllama.fsbot.utilities.exceptions.OutfitNotFoundException;
import org.javacord.api.entity.channel.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class OutfitInfoCommand extends Command {
    @Override public String name() { return "Outfit Info"; }
    @Override public String description() { return "Shows all related info about the outfit"; }
    @Override public String usage() { return "outfitinfo <String id>"; }
    @Override public String category() { return "Staff"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.ROLE, BotConfig.STAFF_ROLE); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("outfitinfo", "oinfo")); }

    private final DatabaseUtilities db;

    public OutfitInfoCommand(DatabaseUtilities db) {
        this.db = db;
    }

    @Override
    public void onCommand(CommandContext ctx) {
        String[] args = ctx.getArgs();
        DiscordApi api = ctx.getApi();
        TextChannel channel = ctx.getChannel();

        if (args.length != 1) {
            channel.sendMessage(String.format("you did that wrong. bother %s to make these more specific.", api.getOwner().join().getDiscriminatedName()));
            return;
        }

        try {
            channel.sendMessage(db.getOutfitFromId(args[0]).generateInfo());
        }
        catch (OutfitNotFoundException e) {
            channel.sendMessage(new DiscordError(e.getMessage()).get());
        }
    }
}
