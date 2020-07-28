/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.listeners;

import com.vdurmont.emoji.EmojiParser;
import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.database.controllers.GalleryController;
import dev.salmonllama.fsbot.database.controllers.OutfitController;
import dev.salmonllama.fsbot.database.models.Outfit;
import dev.salmonllama.fsbot.endpoints.imgur.ImgurAPIConnection;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.util.logging.ExceptionLogger;

import java.sql.Timestamp;

public class ImageListener implements MessageCreateListener {

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
                                                .setDeleteHash(upload.getDeleteHash())
                                                .setCreated(new Timestamp(upload.getDateTime()));

                                        GalleryController.getTag(channel.getIdAsString()).thenAccept(tag -> {
                                            outfitBuilder.setTag(tag);
                                            Outfit outfit = outfitBuilder.build();

                                            OutfitController.insert(outfit).thenAcceptAsync((Void) -> {
                                                // Log the outfit
                                                event.getApi().getServerTextChannelById(BotConfig.OUTFIT_LOG).ifPresentOrElse(chnl -> {
                                                    EmbedBuilder response = new EmbedBuilder()
                                                            .setTitle("Outfit Added")
                                                            .setAuthor(event.getMessageAuthor())
                                                            .setThumbnail(outfit.getLink())
                                                            .setFooter(String.format("%s | %s", outfit.getTag(), outfit.getId()))
                                                            .setUrl(outfit.getLink())
                                                            .addField("Uploaded:", outfit.getCreated().toString());

                                                    if (!outfit.getMeta().equals("")) {
                                                        response.addField("Meta:", outfit.getMeta());
                                                    }

                                                    chnl.sendMessage(response);

                                                    // Add the reaction to the original message
                                                    GalleryController.getEmoji(channel.getIdAsString()).thenAcceptAsync(emoji -> {
                                                        event.getMessage().addReaction(EmojiParser.parseToUnicode(emoji));
                                                    }).exceptionally(ExceptionLogger.get());
                                                }, () -> {
                                                    // Fallback error message to me
                                                    event.getApi().getUserById(BotConfig.BOT_OWNER).thenAcceptAsync(user -> {
                                                        user.sendMessage("Could not find OUTFIT LOG");
                                                    });
                                                });
                                            });
                                        });
                                    }).exceptionally(ExceptionLogger.get());
                                });
                    }
                }
            }).exceptionally(ExceptionLogger.get());
        });
    }
}