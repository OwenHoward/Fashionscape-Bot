/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.listeners;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import com.vdurmont.emoji.EmojiParser;
import dev.salmonllama.fsbot.database.controllers.GalleryController;
import dev.salmonllama.fsbot.database.controllers.OutfitController;
import dev.salmonllama.fsbot.database.models.Outfit;
import dev.salmonllama.fsbot.endpoints.imgur.ImgurAPIConnection;
import okhttp3.*;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.util.logging.ExceptionLogger;
import org.json.JSONObject;
import dev.salmonllama.fsbot.config.BotConfig;

import java.awt.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ImageListener implements MessageCreateListener {

    final RethinkDB r;
    final Connection conn;

    public ImageListener(RethinkDB r, Connection conn) {
        this.r = r;
        this.conn = conn;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) { // TODO: This needs immediate help
        // Check for valid source -> DONE
        // Check for gallery channel presence
        // Check for images (attached files and links from approved sources)
        // Upload the image(s) to imgur
        // Store the image in the database
        // Send confirmation && log event

        if (!event.getMessageAuthor().isRegularUser()) {
            // Ignore anything that is a webhook or a bot message
            return;
        }

        // Only works in Server Text Channels
        event.getChannel().asServerTextChannel().ifPresent(channel -> {
            // Only works in registered Gallery Channels
            GalleryController.galleryExists(channel.getIdAsString()).thenAccept(exists -> {
                if (exists) {
                    // Check the message for images
                    if (event.getMessageAttachments().stream().anyMatch(MessageAttachment::isImage)) {
                        // Upload the image(s) to Imgur, store in database, log the stored images.
                        ImgurAPIConnection imgur = new ImgurAPIConnection();

                        event.getMessageAttachments()
                                .stream()
                                .filter(MessageAttachment::isImage)
                                .forEach(image -> {
                                    // Upload to Imgur
                                    imgur.uploadImage(image.getUrl().toString()).thenAccept(upload -> {
                                        // Store in the database
                                        Outfit.OutfitBuilder outfitBuilder = new Outfit.OutfitBuilder()
                                                .setId(upload.getId())
                                                .setLink(upload.getLink())
                                                .setSubmitter(event.getMessageAuthor().getIdAsString())
                                                .setCreated(new Timestamp(upload.getDateTime()));

                                        GalleryController.getTag(channel.getIdAsString()).thenAccept(outfitBuilder::setTag).join();

                                        Outfit outfit = outfitBuilder.build();
                                        OutfitController.insert(outfit).join();

                                        // Log the event
                                        // event.getChannel().sendMessage("Outfit stored: " + outfit.toString()); // TODO: Logging. Log this to OutfitLog
                                    }).exceptionally(ExceptionLogger.get());
                                });
                    }
                }
            }).exceptionally(ExceptionLogger.get());
        });
    }
}