/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.general;

import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;
import dev.salmonllama.fsbot.utilities.ColorRole;
import dev.salmonllama.fsbot.utilities.database.RoleColourUtility;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import dev.salmonllama.fsbot.utilities.warnings.Warning;
import org.javacord.api.entity.user.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ColorCommand extends Command {
    @Override public String name() { return "Color"; }
    @Override public String description() { return "Assigns the provided cosmetic role"; }
    @Override public String usage() { return "color <colorName>"; }
    @Override public String category() { return "General"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.NONE); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("color", "colour")); }

    @Override
    public void onCommand(CommandContext ctx) {
        if (!ctx.getServer().isPresent()) {
            ctx.reply("This command must be used inside a server.");
            return;
        }
        String[] args = ctx.getArgs();
        DiscordApi api = ctx.getApi();
        Server server = ctx.getServer().get();
        User user = ctx.getUser();
        TextChannel channel = ctx.getChannel();

        // Live server
        if (!server.getIdAsString().equals(BotConfig.HOME_SERVER)) { return; }

        if (!user.getRoles(server).contains(server.getRoleById(BotConfig.HYDRIX_ROLE).orElseThrow(AssertionError::new))) {
            channel.sendMessage(new Warning("You need the Hydrix role to use this").sendWarning());
            return;
        }

        if (args.length != 1) {
            channel.sendMessage(new Warning("Not enough arguments provided").sendWarning());
            return;
        }

        // args[0] is the subcommand, either list, or a colour
        if (args[0].equals("list")) {
            System.out.println(RoleColourUtility.getAllRoleInfo());
            return;
        }

        String roleId = RoleColourUtility.getColour(args[0]);
        Role role;

        if (!api.getRoleById(roleId).isPresent()) {
            channel.sendMessage(new Warning("That role doesn't exist, mate!").sendWarning());
            return;
        }
        else {
            role = api.getRoleById(roleId).get();
        }

        // Actual command logic now

        // Remove all the colour roles from the user, except the target one
        List<ColorRole> allRoles = RoleColourUtility.getAllRoles();
        allRoles.forEach(r -> {
            if (r.id.equals(roleId)) {
                server.removeRoleFromUser(user, server.getRoleById(r.id).orElseThrow(AssertionError::new));
            }
        });

        // If user already has the role, remove it
        if (user.getRoles(server).contains(role)) {
            user.removeRole(role);
            channel.sendMessage(new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .setTitle("Role Removed")
                    .addInlineField("Role:", role.getName())
            );
        }
        else {
            user.addRole(role);
            channel.sendMessage(new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .setTitle("Role Added")
                    .addInlineField("Role:", role.getName())
            );
        }
    }
}
