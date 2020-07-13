/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.logging;

import dev.salmonllama.fsbot.config.BotConfig;
import org.javacord.api.DiscordApi;

public class Logger {

    private final DiscordApi api;

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
            channel.sendMessage("outfit");
        }, () -> {
            // DM me
            api.getUserById(SALMONLLAMA).thenAcceptAsync(user -> {
                user.sendMessage("Outfit log failed and was not found");
            });
        });
    }

    public void logReport() {
        api.getServerTextChannelById(REPORT_LOG).ifPresentOrElse(channel -> {
            // Log the thing
            channel.sendMessage("report");
        }, () -> {
            // DM me
            api.getUserById(SALMONLLAMA).thenAcceptAsync(user -> {
                user.sendMessage("Report log failed and was not found");
            });
        });
    }

    public void logError() {
        api.getServerTextChannelById(BOT_LOG).ifPresentOrElse(channel -> {
            // Log the thing
            channel.sendMessage("error");
        }, () -> {
            // DM me
            api.getUserById(SALMONLLAMA).thenAcceptAsync(user -> {
                user.sendMessage("Error log failed and was not found");
            });
        });
    }

    public void logMovement() {
        api.getServerTextChannelById(JOIN_LOG).ifPresentOrElse(channel -> {
            // Log the thing
            channel.sendMessage("User joined/Left");
        }, () -> {
            // DM me
            api.getUserById(SALMONLLAMA).thenAcceptAsync(user -> {
                user.sendMessage("Movement log failed and was not found");
            });
        });
    }
}
