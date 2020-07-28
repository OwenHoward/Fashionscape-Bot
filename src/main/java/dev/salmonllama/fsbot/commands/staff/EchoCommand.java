/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.staff;

import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class EchoCommand extends Command {
    @Override public String name() { return "Echo"; }
    @Override public String description() { return "Echos your message. Typical bash"; }
    @Override public String usage() { return "echo <message>"; }
    @Override public String category() { return "Staff"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.STATIC, "staff"); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Collections.singletonList("echo")); }

    @Override
    public void onCommand(CommandContext ctx) {
        String echoMessage = String.join(" ", ctx.getArgs());

        ctx.getMessage().delete();

        ctx.reply(echoMessage);
    }
}
