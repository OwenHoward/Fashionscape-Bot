/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.staff;

import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.database.controllers.ServerConfigController;
import dev.salmonllama.fsbot.database.models.ServerConfig;
import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;
import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.entity.message.embed.EmbedBuilder;

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
        if (ctx.isPrivateMessage()) {
            ctx.reply("This command can only be used within a server");
            return;
        }

        if (ctx.getArgs().length < 1) {
            ctx.reply("You need to supply arguments for this command");
            return;
        }

        String[] args = ctx.getArgs();
        switch (args[0]) {
            case "get":
                get(ctx);
            case "set": // TODO: check for args here
                set(ctx, args[1]);
        }
    }

    private void get(CommandContext ctx) {
        ServerConfigController.get(ctx.getServer().get().getIdAsString()).thenAcceptAsync(possibleConf -> {
            possibleConf.ifPresentOrElse(conf -> {
                EmbedBuilder response = new EmbedBuilder()
                        .setTitle("Current Welcome Message")
                        .setDescription(conf.getWelcomeMessage());

                ctx.reply(response);
            }, () -> {
                EmbedBuilder response = new EmbedBuilder()
                        .setTitle("Does not exist!")
                        .setDescription("No welcome message was found! use `~wmsg set` to set one!");

                ctx.reply(response);
            });
        });
    }

    private void set(CommandContext ctx, String newMsg) {
        ServerConfigController.get(ctx.getServer().get().getIdAsString()).thenAcceptAsync(possibleConf -> {
            possibleConf.ifPresentOrElse(conf -> {
                // Update the config
                ServerConfig config = new ServerConfig.ServerConfigBuilder().from(conf)
                        .setWelcomeMessage(newMsg)
                        .build();

                ServerConfigController.update(config);

                EmbedBuilder response = new EmbedBuilder()
                        .setTitle("Welcome Message Set")
                        .addField("New Welcome Message:", config.getWelcomeMessage());

                ctx.reply(response);
            }, () -> {
                // Create a config and set the welcome message
                ServerConfig config = new ServerConfig.ServerConfigBuilder()
                        .setId(ctx.getServer().get().getIdAsString())
                        .setPrefix(BotConfig.DEFAULT_PREFIX)
                        .setWelcomeMessage(newMsg)
                        .build();

                ServerConfigController.insert(config);

                EmbedBuilder response = new EmbedBuilder()
                        .setTitle("Welcome Message Set!")
                        .setDescription("server conf has been created")
                        .addField("New Welcome Message:", newMsg);

                ctx.reply(response);
            });
        });
    }
}
