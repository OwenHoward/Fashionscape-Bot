/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.general;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;

import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.database.controllers.GalleryController;
import dev.salmonllama.fsbot.database.models.GalleryChannel;
import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;

public class ShowGalleriesCommand extends Command {
    @Override public String name() { return "Show Galleries"; }
    @Override public String description() { return "Shows registered gallery channels in the current server"; }
    @Override public String usage() { return "showgalleries"; }
    @Override public String category() { return "General"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.ROLE, BotConfig.STAFF_ROLE); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("showgalleries", "listgalleries")); }

    @Override public void onCommand(CommandContext ctx) {
        if (ctx.isPrivateMessage()) {
            ctx.reply("This command can only be used within a server"); // TODO: Preset embeds again, yeah
            return;
        }

        ctx.getServer().ifPresent(server -> {
            try {
                Collection<GalleryChannel> galleries = GalleryController.getGalleriesByServer(server.getIdAsString());
                ctx.reply(galleryEmbed(galleries, server));
            } catch (SQLException e) {
                ctx.reply("An exception has occurred: " + e.getMessage());
            }
            
        });
    }

    EmbedBuilder galleryEmbed(Collection<GalleryChannel> galleries, Server server) { // TODO: Base FSBot embed.
        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("Server Gallery Channels");

        Collection<String> mentionTags = galleries.stream().map(
                galleryChannel -> server.getChannelById(
                        galleryChannel.getChannelId()).get() // Can call get safely -> The channel is not stored if it is not a valid ServerTextChannel
                        .asServerTextChannel().get()) // Same as above
                .map(ServerTextChannel::getMentionTag).collect(Collectors.toList());

        Collection<String> tags = galleries.stream().map(GalleryChannel::getTag).collect(Collectors.toList());

        embed.addField("Channels", String.join("\n", mentionTags), true);
        embed.addField("Tags", String.join("\n", tags), true);

        return embed;
    }
}
