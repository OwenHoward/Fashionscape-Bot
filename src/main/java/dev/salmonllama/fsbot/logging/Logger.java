/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.logging;

import dev.salmonllama.fsbot.config.BotConfig;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;

import java.util.concurrent.CompletableFuture;

public class Logger {

    private DiscordApi api;

    private final String OUTFIT_LOG = BotConfig.OUTFIT_LOG;
    private final String REPORT_LOG = BotConfig.REPORT_LOG;
    private final String JOIN_LOG = BotConfig.JOIN_LOG;
    private final String BOT_LOG = BotConfig.BOT_LOG;
    private final String SALMONLLAMA = BotConfig.BOT_OWNER;

    public Logger(DiscordApi api) {
        this.api = api;
    }

    public void logOutfit() {
        api.getServerTextChannelById(OUTFIT_LOG).ifPresentOrElse(channel -> {
            // Log the thing
            channel.sendMessage("thing");
        }, () -> {
            // Do something else
            api.getUserById(SALMONLLAMA).thenAcceptAsync(user -> {
                user.sendMessage("Outfit log failed and was not found");
            });
        });
    }

    public void logReport() {

    }

    public static void error() {

    }
}
