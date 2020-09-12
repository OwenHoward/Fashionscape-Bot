/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.general;

import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.database.controllers.ColorRoleController;
import dev.salmonllama.fsbot.guthix.*;
import org.apache.logging.log4j.util.Strings;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.Arrays;
import java.util.List;

public class ColorCommand extends Command {
    @Override public String name() { return "Color"; }
    @Override public String description() { return "Assigns the provided cosmetic role"; }
    @Override public String usage() { return "color <colorName>"; }
    @Override public CommandCategory category() { return CommandCategory.GENERAL; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.NONE); }
    @Override public List<String> aliases() { return Arrays.asList("color", "colour"); }

    @Override
    public void onCommand(CommandContext ctx) {
        // The color given will be the args.
        // Check if args[0] is reset. If so, remove all color roles.
        // Check if the given string is a current color role.
        // Remove any color roles that the user has.
        // Give the user the color role they specified
        ctx.getServer().ifPresentOrElse(server -> {
            String[] args = ctx.getArgs();
            if (server.getIdAsString().equals(BotConfig.HOME_SERVER)) {
                if (args[0].equals("reset")) {
                    // Remove all color roles
                    removeColorRoles(ctx.getUser(), server);
                }

                String color = Strings.join(Arrays.asList(args), ' ');
                ColorRoleController.get(color).thenAcceptAsync(possibleColorRole -> possibleColorRole.ifPresentOrElse(colorRole -> {
                    // Remove all color roles
                    removeColorRoles(ctx.getUser(), server);
                    // Give the user the one they specified
                    addColorRole(ctx.getUser(), server, colorRole.getRoleId());
                }, () -> ctx.reply("That color does not exist")));
            }
        }, () -> ctx.reply("This command can only be used in the fashionscape server"));
    }

    private static void removeColorRoles(User user, Server server) {
        ColorRoleController.getAll().thenAcceptAsync(
                possibleRoles -> possibleRoles.ifPresent(
                        colorRoles -> colorRoles.forEach(
                                colorRole -> server.getRoleById(colorRole.getRoleId()).ifPresent(
                                                role -> user.removeRole(role, "Removed color role")
                                )
                        )
                )
        );
    }

    private static void addColorRole(User user, Server server, long roleId) {
        server.getRoleById(roleId).ifPresent(role -> user.addRole(role, "Added color role"));
    }
}
