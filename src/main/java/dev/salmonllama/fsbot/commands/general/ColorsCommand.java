/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.general;

import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.database.controllers.ColorRoleController;
import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;
import org.javacord.api.entity.message.embed.EmbedBuilder;

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
        // List available color roles
        ctx.getServer().ifPresentOrElse(server -> {
            if (server.getIdAsString().equals(BotConfig.HOME_SERVER)) {
                ColorRoleController.getAll().thenAcceptAsync(
                        possibleColorRoles -> possibleColorRoles.ifPresentOrElse(colorRoles -> {
                            EmbedBuilder response = new EmbedBuilder()
                                    .setTitle("Color roles")
                                    .setFooter(String.format("Found %d roles", colorRoles.size()));

                            colorRoles.forEach(
                                    colorRole -> server.getRoleById(colorRole.getRoleId()).ifPresent(
                                        role -> response.addField(colorRole.getColor(), role.getMentionTag(), true)
                                    )
                            );

                            ctx.reply(response);
                        }, () -> ctx.reply("No color roles have been found")));
            } else {
                ctx.reply("This command can only be used in the fashionscape server");
            }
        }, () -> ctx.reply("This command can only be used in the fashionscape server"));
    }
}
