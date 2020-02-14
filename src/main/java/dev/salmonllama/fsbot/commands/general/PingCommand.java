/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.general;

import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;

import java.util.*;

public class PingCommand extends Command {
    @Override public String name() { return "Ping"; }
    @Override public String description() { return "Pings the bot, checks for a heartbeat"; }
    @Override public String usage() { return "ping"; }
    @Override public String category() { return "General"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.NONE); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Collections.singletonList("ping")); }

    @Override
    public void onCommand(CommandContext ctx) {
        final long start = System.currentTimeMillis();

        ctx.getChannel().sendMessage("I'm alive").thenAccept(msg -> {
            final long end = System.currentTimeMillis();
            msg.edit(String.format("I'm alive! (%sms)", (end - start)));
        });
    }
}
