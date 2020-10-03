package dev.salmonllama.fsbot.utilities;

import dev.salmonllama.fsbot.config.BotConfig;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;

public class AddMemberRole implements Runnable{

    ServerMemberJoinEvent event;

    public AddMemberRole(ServerMemberJoinEvent event) {
        this.event = event;
    }

    @Override
    public void run() {
        event.getApi().getRoleById(BotConfig.MEMBER_ROLE).ifPresent(role -> role.addUser(event.getUser()));
    }
}
