/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.listeners;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.MessageBuilder;
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
        TextChannel channel = event.getChannel();

        if (!channel.getIdAsString().equals(BotConfig.REPORT_CHANNEL)) {
            return;
        }

        Message message = event.getMessage();
        MessageAuthor author = message.getAuthor();

        if (author.isBotUser()) {
            return;
        }

        String content = message.getContent();
        message.getAttachments().forEach(
                attachment -> attachment.downloadAsImage().thenAcceptAsync(
                        image -> new MessageBuilder()
                            .addAttachment(image, "evidence")
                            .setContent("Report Evidence:")
                            .send(channel)));

        message.delete().join();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("User Report")
                .setDescription(content)
                .setColor(Color.GREEN)
                .setFooter("From: " + author.getDiscriminatedName());

        Optional<TextChannel> logChannel = api.getTextChannelById(BotConfig.REPORT_LOG);
        if (logChannel.isPresent()) {
            logChannel.get().sendMessage(embed);
        }
        else {
            channel.sendMessage("Could not find proper log channel, please contact a staff member.")
                    .thenAccept(MessageUtilities.deleteAfter(30, TimeUnit.SECONDS));

        }

    }
}
