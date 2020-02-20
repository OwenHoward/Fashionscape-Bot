/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.general;

import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;
import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.utilities.ColorRole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ColorsCommand extends Command {
    @Override public String name() { return "Colors"; }
    @Override public String description() { return "Lists available cosmetic roles"; }
    @Override public String usage() { return "colors"; }
    @Override public String category() { return "General"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.NONE); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("colors", "colours")); }

    @Override
    public void onCommand(CommandContext ctx) {
        if (!ctx.getServer().isPresent()) {
            ctx.reply("This command must be used in a server.");
            return;
        }
        Server server = ctx.getServer().get();
        TextChannel channel = ctx.getChannel();

        if (!server.getIdAsString().equals(BotConfig.HOME_SERVER)) {
            return;
        }

        channel.sendMessage(ColorRole.rolesListEmbed());
    }
}
