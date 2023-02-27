/*
 * Copyright (c) 2021 Aleksei Gryczewski
 */

package dev.salmonllama.fsbot.listeners;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.utilities.MessageUtilities;

import java.awt.*;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ReportListener implements MessageCreateListener {

    public void onMessageCreate(MessageCreateEvent event) {
        DiscordApi api = event.getApi();
        TextChannel reportChannel = event.getChannel();

        if (!reportChannel.getIdAsString().equals(BotConfig.REPORT_CHANNEL)) {
            return;
        }

        Message message = event.getMessage();
        String content = message.getContent();
        MessageAuthor author = message.getAuthor();

        if (author.isBotUser()) {
            return;
        }

        // The report log, also known as the #moderators channel.
        Optional<TextChannel> modChannel = api.getTextChannelById(BotConfig.REPORT_LOG);

        if (message.getAttachments().stream().anyMatch(MessageAttachment::isImage)) {
            // Send the images with the content
            message.getAttachments().stream().filter(MessageAttachment::isImage).forEach(
                    messageAttachment -> {
                        // The messageAttachment is an image, proceed accordingly.
                        // Upload the image to an embed and send it to the mods channel
                        // Include any content that was sent with it
                        messageAttachment.asImage().thenAcceptAsync(bufferedImage -> {
                            EmbedBuilder embed = new EmbedBuilder()
                                    .setTitle("User Report with Image")
                                    .setColor(Color.GREEN)
                                    .setImage(bufferedImage)
                                    .setFooter("Sent by: " + author.getDiscriminatedName());

                            if (!message.getContent().equals("")) {
                                embed.setDescription(message.getContent());
                            }

                            modChannel.ifPresentOrElse(
                                    chnl -> chnl.sendMessage(embed),
                                    () -> reportChannel.sendMessage("An error has occurred. Could not find the proper log channel. Please contact a staff member")
                                            .thenAccept(MessageUtilities.deleteAfter(30, TimeUnit.SECONDS)));
                        });
                    });
        } else {
            // Just send the content
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("User Report")
                    .setDescription(content)
                    .setColor(Color.GREEN)
                    .setFooter("From: " + author.getDiscriminatedName());

            modChannel.ifPresentOrElse(
                    chnl -> chnl.sendMessage(embed),
                    () -> reportChannel.sendMessage("An error has occurred. Could not find the proper log channel. Please contact a staff member")
                            .thenAccept(MessageUtilities.deleteAfter(30, TimeUnit.SECONDS)));
        }

        // Delete the message from the channel.
        message.delete().join();
    }
}
