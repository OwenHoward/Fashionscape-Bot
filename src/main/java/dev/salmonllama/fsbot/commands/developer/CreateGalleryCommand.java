/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.developer;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.database.controllers.GalleryController;
import dev.salmonllama.fsbot.database.models.GalleryChannel;
import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class CreateGalleryCommand extends Command { // TODO: This command needs help.
    @Override public String name() { return "Create Gallery"; }
    @Override public String description() { return "Creates a channel gallery, tracking any posted images"; }
    @Override public String usage() { return "creategallery <String tag>"; }
    @Override public String category() { return "Developer"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.OWNER); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("creategallery", "addgallery", "newgallery")); }

    static final RethinkDB R = RethinkDB.r;
    static final Connection CONN = R.connection().hostname("localhost").port(28015).connect();

    @Override
    public void onCommand(CommandContext ctx) { // TODO: Might need some logic help...
        if (ctx.isPrivateMessage()) {
            ctx.reply("This command can only be used in a server!"); // TODO: Stop this. Turn this into a preset no-no embed.
            return;
        }
        if (ctx.getArgs().length != 1) {
            ctx.reply("Args are incorrect");
            return;
        }
        // Check if the channel is already a registered gallery channel.
        // Create a gallery channel of the current channel.
        // Store the gallery channel in the database.

        String channelId = ctx.getChannel().getIdAsString();
        try {
            if (GalleryController.galleryExists(channelId)) {
                ctx.reply("A gallery already exists in this channel, can not create a new one.");
                return;
            }
        } catch (SQLException e) {
            ctx.reply(String.format("Error: %s", e.getMessage())); // TODO: error logging.
            return;
        }

        String tag = ctx.getArgs()[0];

        GalleryChannel gallery = new GalleryChannel();
        gallery.channelId = channelId;
        gallery.tag = tag;

        ctx.getServer().ifPresent(server -> {
            gallery.serverId = server.getIdAsString();
            gallery.serverName = server.getName();
        });

        ctx.getChannel().asServerTextChannel().ifPresent(channel -> gallery.channelName = channel.getName());

        try {
            GalleryController.insert(gallery);
        } catch(SQLException e) {
            ctx.reply(String.format("Error: %s", e.getMessage())); // TODO: error logging.
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .addField("Success", "Gallery has been created:")
                .addField("Channel Name:", gallery.channelName)
                .addField("Channel Id:", gallery.channelId)
                .addField("Tag:", tag)
                .addField("End:", String.format("This channel is now being tracked under: %s", tag));
        ctx.getChannel().sendMessage(embed);
        }
}

