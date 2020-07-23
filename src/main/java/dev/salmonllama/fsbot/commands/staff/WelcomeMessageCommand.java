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
import dev.salmonllama.fsbot.utilities.warnings.Warning;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class WelcomeMessageCommand extends Command {
    @Override public String name() { return "Welcome Message"; }
    @Override public String description() { return "View or update the server welcome message. Options: get|set|getchannel|setchannel."; }
    @Override public String usage() { return "welcomemessage <String opt> [String newMessage]"; }
    @Override public String category() { return "Staff"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.ROLE, BotConfig.STAFF_ROLE); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("welcomemessage", "wmsg")); }

    @Override
    public void onCommand(CommandContext ctx) {
        ctx.reply("This command is a WIP and will be available soon.");
    }
}
