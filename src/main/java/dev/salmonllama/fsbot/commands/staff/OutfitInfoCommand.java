/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.staff;

import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.database.controllers.OutfitController;
import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;
import org.javacord.api.DiscordApi;
import dev.salmonllama.fsbot.utilities.database.DatabaseUtilities;
import dev.salmonllama.fsbot.utilities.exceptions.DiscordError;
import dev.salmonllama.fsbot.utilities.exceptions.OutfitNotFoundException;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;

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

    @Override
    public void onCommand(CommandContext ctx) {
        String[] args = ctx.getArgs();

        if (args.length != 1) {
            ctx.reply("You must supply an outfit ID.");
            return;
        }

        String id = args[0];
        OutfitController.findById(id).thenAcceptAsync(possibleOutfit -> {
            possibleOutfit.ifPresentOrElse(outfit -> {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Outfit Info")
                        .setThumbnail(outfit.getLink())
                        .setAuthor(ctx.getApi().getYourself())
                        .setUrl(outfit.getLink())
                        .setFooter(String.format("Tag: %s", outfit.getTag()))
                        .addField("Added", outfit.getCreated().toString(), true)
                        .addField("Updated", outfit.getUpdated().toString(), true)
                        .addField("Submitted by:", ctx.getApi().getUserById(outfit.getSubmitter()).join().getDiscriminatedName())
                        .addField("Deleted", outfit.isDeleted() ? "True" : "False", true)
                        .addField("Featured", outfit.isFeatured() ? "True" : "False", true);
                ctx.reply(embed);
            }, () -> {
                ctx.reply("Outfit not found");
            });
        });
    }
}
