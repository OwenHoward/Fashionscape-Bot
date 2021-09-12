/*
 * Copyright (c) 2021 Aleksei Gryczewski
 */

package dev.salmonllama.fsbot.listeners;

import com.vdurmont.emoji.EmojiParser;
import dev.salmonllama.fsbot.config.BotConfig;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class ThumbsListener implements MessageCreateListener {

    public void onMessageCreate(MessageCreateEvent event) {

        if (event.getChannel().getIdAsString().equals(BotConfig.ANNOUNCEMENT_CHANNEL)) {
            // Announcements
            event.getMessage().addReaction(EmojiParser.parseToUnicode(":thumbsup:"));
            event.getMessage().addReaction(EmojiParser.parseToUnicode(":thumbsdown:"));
        }
        else if (event.getChannel().getIdAsString().equals(BotConfig.NEWS_CHANNEL)) {
            // Newsfeed
            event.getMessage().addReaction(EmojiParser.parseToUnicode(":thumbsup:"));
            event.getMessage().addReaction(EmojiParser.parseToUnicode(":thumbsdown:"));
        }
        else if (event.getChannel().getIdAsString().equals(BotConfig.VOTE_CHANNEL)) {
            // Votes
            event.getMessage().addReaction(EmojiParser.parseToUnicode(":thumbsup:"));
            event.getMessage().addReaction(EmojiParser.parseToUnicode(":thumbsdown:"));
        }


    }

}
