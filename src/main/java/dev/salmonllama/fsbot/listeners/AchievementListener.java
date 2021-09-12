/*
 * Copyright (c) 2021 Aleksei Gryczewski
 */

package dev.salmonllama.fsbot.listeners;

import com.vdurmont.emoji.EmojiParser;
import dev.salmonllama.fsbot.config.BotConfig;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class AchievementListener implements MessageCreateListener {

    public void onMessageCreate(MessageCreateEvent event) {
        if (!event.getChannel().getIdAsString().equals(BotConfig.ACHIEVEMENT_CHANNEL)) {
            return;
        }

        if (event.getMessage().getAttachments().stream().noneMatch(MessageAttachment::isImage)) {
            return;
        }

        event.getMessage().addReaction(EmojiParser.parseToUnicode(":tada:"));
    }
}
