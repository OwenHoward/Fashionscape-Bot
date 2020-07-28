/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.developer;

import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class InviteCommand extends Command {
    @Override public String name() { return "Invite"; }
    @Override public String description() { return "Spits out a bot invite"; }
    @Override public String usage() { return "invite"; }
    @Override public String category() { return "Developer"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.STATIC, "owner"); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("invite", "inv")); }

    @Override
    public void onCommand(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        DiscordApi api = ctx.getApi();

        channel.sendMessage(String.format("<%s>", api.createBotInvite()));
    }
}
