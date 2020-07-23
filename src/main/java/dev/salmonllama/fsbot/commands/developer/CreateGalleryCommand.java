/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.developer;

import com.vdurmont.emoji.EmojiManager;
import dev.salmonllama.fsbot.database.controllers.GalleryController;
import dev.salmonllama.fsbot.database.models.GalleryChannel;
import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.util.logging.ExceptionLogger;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class CreateGalleryCommand extends Command { // TODO: This command needs help.
    @Override public String name() { return "Create Gallery"; }
    @Override public String description() { return "Creates a channel gallery, tracking any posted images"; }
    @Override public String usage() { return "creategallery <String tag>"; }
    @Override public String category() { return "Developer"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.OWNER); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("creategallery", "addgallery", "newgallery")); }

    @Override
    public void onCommand(CommandContext ctx) {
        if (ctx.isPrivateMessage()) {
            ctx.reply("This command can only be used in a server!"); // TODO: Stop this. Turn this into a preset no-no embed.
            return;
        }
        if (ctx.getArgs().length < 1) {
            ctx.reply("Args are incorrect");
            return;
        }
        // Check if the channel is already a registered gallery channel.
        // Create a gallery channel of the current channel.
        // Store the gallery channel in the database.

        String channelId = ctx.getChannel().getIdAsString();
        if (GalleryController.galleryExists(channelId).join()) { // This is a value that is needed immediately.
            ctx.reply("A gallery already exists in this channel, can not create a new one.");
            return;
        }

        String tag = ctx.getArgs()[0];

        GalleryChannel.GalleryBuilder galleryBuilder = new GalleryChannel.GalleryBuilder();
        galleryBuilder.setChannelId(channelId);
        galleryBuilder.setTag(tag);
        galleryBuilder.setEmoji(":heartpulse:");

        ctx.getServer().ifPresent(server -> {
            galleryBuilder.setServerId(server.getIdAsString());
            galleryBuilder.setServerName(server.getName());
        });

        GalleryChannel gallery = galleryBuilder.build();
        GalleryController.insert(gallery).exceptionally(ExceptionLogger.get()); // TODO: Make a discord exception logger for the thingos

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .addField("Success", "Gallery has been created:")
                .addField("Channel Id:", gallery.getChannelId())
                .addField("Tag:", tag)
                .addField("Emoji:", EmojiManager.getByUnicode(gallery.getEmoji()).toString())
                .addField("End:", String.format("This channel is now being tracked under: %s", tag));
        ctx.getChannel().sendMessage(embed);
    }
}

