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
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class GetServersCommand extends Command {
    @Override public String name() { return "Get Servers"; }
    @Override public String description() { return "Lists all the servers the bot is in"; }
    @Override public String usage() { return "getservers"; }
    @Override public String category() { return "Staff"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.ROLE, BotConfig.STAFF_ROLE); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("getservers", "servers")); }

    @Override
    public void onCommand(CommandContext ctx) {
        DiscordApi api = ctx.getApi();

        StringBuilder builder = new StringBuilder();
        builder.append("```md");

        for (Server server : api.getServers()) {
            String serverId = server.getIdAsString();
            String serverName = server.getName();

            builder.append(String.format("\n- %s: %s", serverName, serverId));
        }

        builder.append("\n```");

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("Servers")
                .setDescription(builder.toString())
                .setFooter(String.format("%s is in %d servers", api.getYourself().getName(), api.getServers().size()));

        ctx.getChannel().sendMessage(embed).join();
    }
}
