/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.listeners;

import dev.salmonllama.fsbot.database.controllers.GalleryController;
import dev.salmonllama.fsbot.database.controllers.OutfitController;
import dev.salmonllama.fsbot.database.models.Outfit;
import dev.salmonllama.fsbot.endpoints.imgur.ImgurAPIConnection;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.util.logging.ExceptionLogger;

import java.sql.Timestamp;

public class ImageListener implements MessageCreateListener {

    public ImageListener() {
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) { // TODO: This needs immediate help
        // Check for valid source -> DONE -> WORKING
        // Check for gallery channel presence -> DONE -> WORKING
        // Check for images (attached files and links from approved sources) -> DONE -> WORKING (approved links to be added later)
        // Upload the image(s) to imgur -> DONE -> WORKING
        // Store the image in the database -> DONE -> WORKING
        // Send confirmation && log event -> IN PROGRESS (waiting for logger upgrade)

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
                                                .setMeta(event.getMessageContent())
                                                .setLink(upload.getLink())
                                                .setSubmitter(event.getMessageAuthor().getIdAsString())
                                                .setCreated(new Timestamp(upload.getDateTime()));

                                        GalleryController.getTag(channel.getIdAsString()).thenAccept(outfitBuilder::setTag).join(); // TODO: Wrap this around the insert, don't join no moah

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