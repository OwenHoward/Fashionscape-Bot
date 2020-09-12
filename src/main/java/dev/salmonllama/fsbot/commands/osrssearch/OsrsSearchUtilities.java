/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.osrssearch;

import dev.salmonllama.fsbot.endpoints.scapefashion.ScapeFashionResult;
import dev.salmonllama.fsbot.guthix.CommandContext;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;

public class OsrsSearchUtilities {
    static boolean isColor(String s) {
        return s.startsWith("#");
    }

    static void sendResult(ScapeFashionResult result, TextChannel channel) {
        var bestMatch = result.getItems().get(0);
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(String.format("Best Match - %s", bestMatch.getName()))
                .setImage(bestMatch.getImages().getDetail())
                .setUrl(result.getLink())
                .setDescription(String.format("Click the title or visit %s for full results!", result.getLink()));

        channel.sendMessage(embed);
    }

    static void handleException(Exception e, CommandContext ctx) {
        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("An error has occurred!")
            .setDescription(e.getMessage());

            ctx.reply(embed);
    }
}
