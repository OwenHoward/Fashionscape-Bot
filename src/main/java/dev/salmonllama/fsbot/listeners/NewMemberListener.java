/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.listeners;

import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.database.controllers.ServerConfigController;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;

public class NewMemberListener implements ServerMemberJoinListener {

    public void onServerMemberJoin(ServerMemberJoinEvent event) {

        if (!event.getServer().getIdAsString().equals(BotConfig.HOME_SERVER)) {
            // Only active in the Fashionscape server, currently.
            return;
        }

        // TODO: Use the ServerConfig to retrieve the welcome channel as well.
        event.getApi().getServerTextChannelById(BotConfig.WELCOME_CHANNEL).ifPresent( // Get the Welcome Channel
                channel -> ServerConfigController.get(event.getServer().getIdAsString()).thenAcceptAsync( // Fetch the server config, if set.
                        possibleConfig -> possibleConfig.ifPresent( // If config exists
                                config -> channel.sendMessage(String.format(config.getWelcomeMessage(), event.getUser().getMentionTag()))))); // Send the welcome message
    }
}
