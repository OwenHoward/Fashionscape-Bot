/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.utilities;

import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.guthix.CommandContext;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;
import java.util.Arrays;

public class DiscordUtilities {
    public static void handleException(Exception e, CommandContext ctx) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Exception caught in command thread:")
                .addField("message:", e.getMessage())
                .setColor(Color.RED);

        logException(e, ctx);
        ctx.reply(embed);
    }

    private static void logException(Exception e, CommandContext ctx) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Runtime error:")
                .addField("Message:", e.getMessage())
                .setColor(Color.RED);

        ctx.getApi().getTextChannelById(BotConfig.ACTIVITY_LOG).ifPresent(channel -> channel.sendMessage(embed));
    }

//    public static void report(Class<? extends Throwable>... throwable) {
//        System.out.println(Arrays.stream(throwable));
//    }
}
