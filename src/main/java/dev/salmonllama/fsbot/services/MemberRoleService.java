/*
 * Copyright (c) 2021 Aleksei Gryczewski
 */

package dev.salmonllama.fsbot.services;

import dev.salmonllama.fsbot.config.BotConfig;

import org.javacord.api.event.server.member.ServerMemberJoinEvent;

public class MemberRoleService implements Runnable {

    ServerMemberJoinEvent event;

    public MemberRoleService(ServerMemberJoinEvent event) {
        this.event = event;
    }

    @Override
    public void run() {
        event.getApi().getRoleById(BotConfig.MEMBER_ROLE).ifPresent(role ->
            event.getServer().addRoleToUser(event.getUser(), role, "FSBot 5-minute member role")
        );
    }
}
