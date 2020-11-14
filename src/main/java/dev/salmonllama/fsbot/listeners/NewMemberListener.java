/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.listeners;

import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.database.controllers.ServerConfigController;
import dev.salmonllama.fsbot.services.MemberRoleService;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;

import java.util.concurrent.TimeUnit;

public class NewMemberListener implements ServerMemberJoinListener {
    public void onServerMemberJoin(ServerMemberJoinEvent event) {

        if (!event.getServer().getIdAsString().equals(BotConfig.HOME_SERVER)) {
            // Only active in the Fashionscape server, currently.
            return;
        }

        // Send the welcome message in the welcome channel
        event.getApi().getServerTextChannelById(BotConfig.WELCOME_CHANNEL).ifPresent( // Get the Welcome Channel
                channel -> ServerConfigController.get(event.getServer().getIdAsString()).thenAcceptAsync( // Fetch the server config, if set.
                        possibleConfig -> possibleConfig.ifPresent( // If config exists
                                config -> channel.sendMessage(String.format(config.getWelcomeMessage(), event.getUser().getMentionTag()))))); // Send the welcome message

        // Add the Member role after 5 minutes
        var memberRoleService = new MemberRoleService(event);
        event.getApi().getThreadPool().getScheduler().schedule(memberRoleService, 5, TimeUnit.MINUTES);
    }
}
