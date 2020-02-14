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
import dev.salmonllama.fsbot.utilities.database.ServerConfUtility;
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
        String[] args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        Server server = ctx.getServer();

        if (!server.getIdAsString().equals(BotConfig.HOME_SERVER)) {
            return;
        }

        if (args.length == 0) channel.sendMessage(new Warning("Not enough arguments provided").sendWarning());

        switch (args[0]) {
            case ("get"):
                channel.sendMessage(fetchWelcomeMsg(server));
                break;
            case ("set"):
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    sb.append(String.format("%s ", args[i]));
                }

                channel.sendMessage(updateWelcomeMsg(sb.toString(), server));
                break;
            case ("getchannel"):
                channel.sendMessage(fetchWelcomeChannel(server));
                break;
            case ("setchannel"):
                if (args.length < 2) channel.sendMessage(new Warning("Not enough arguments provided").sendWarning());

                channel.sendMessage(updateWelcomeChannel(args[1], server));
                break;
        }
    }

    private EmbedBuilder fetchWelcomeMsg(Server server) {

        ServerConfUtility conf = new ServerConfUtility(server.getIdAsString());
        String msg = conf.getWelcomeMsg();

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("Current welcome message:")
                .setDescription(msg);
    }

    private EmbedBuilder updateWelcomeMsg(String msg, Server server) {

        ServerConfUtility conf = new ServerConfUtility(server.getIdAsString());
        conf.setWelcomeMsg(msg);

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("Welcome message updated")
                .addField("New welcome message:", msg);
    }

    private EmbedBuilder fetchWelcomeChannel(Server server) {

        ServerConfUtility conf = new ServerConfUtility(server.getIdAsString());
        String welcomeChannel = conf.getWelcomeChannel();

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("Current welcome channel:")
                .setDescription(welcomeChannel);
    }

    private EmbedBuilder updateWelcomeChannel(String id, Server server) {

        ServerConfUtility conf = new ServerConfUtility(server.getIdAsString());
        conf.setWelcomeChannel(id);

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("Welcome channel updated:")
                .addField("New welcome channel:", id);
    }
}
