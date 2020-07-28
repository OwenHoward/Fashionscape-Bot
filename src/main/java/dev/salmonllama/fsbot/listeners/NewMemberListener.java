/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.listeners;

import dev.salmonllama.fsbot.config.BotConfig;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;

public class NewMemberListener implements ServerMemberJoinListener {

    public void onServerMemberJoin(ServerMemberJoinEvent event) {

        if (!event.getServer().getIdAsString().equals(BotConfig.HOME_SERVER)) {
            return;
        }

//        String logMessage = String.format(welcomeMsg, event.getUser().getMentionTag());

        event.getApi().getServerTextChannelById(BotConfig.WELCOME_CHANNEL).ifPresent(channel -> channel.sendMessage("Welcome!"));
    }
}
