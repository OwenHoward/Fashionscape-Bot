/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.listeners;

import dev.salmonllama.fsbot.utilities.database.ServerConfUtility;
import org.javacord.api.DiscordApi;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;

public class NewMemberListener implements ServerMemberJoinListener {

    private DiscordApi api;

    public NewMemberListener(DiscordApi api) {
        this.api = api;
    }

    public void onServerMemberJoin(ServerMemberJoinEvent event) {

        if (!event.getServer().getIdAsString().equals("340511685024546816")) return;

        ServerConfUtility conf = new ServerConfUtility(event.getServer().getIdAsString());
        String welcomeMsg = conf.getWelcomeMsg();
        String welcomeChannel = conf.getWelcomeChannel();

        String logMessage = String.format(welcomeMsg, event.getUser().getMentionTag());

        api.getChannelById(welcomeChannel).ifPresent(chnl -> {
            chnl.asServerTextChannel().ifPresent(channel -> {
                channel.sendMessage(logMessage);
            });
        });
    }
}
