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
import dev.salmonllama.fsbot.utilities.database.RoleColourUtility;
import dev.salmonllama.fsbot.utilities.warnings.Warning;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class AddColorCommand extends Command {
    @Override public String name() { return "Add Color"; }
    @Override public String description() { return "adds the provided role to the toggleable cosmetic roles."; }
    @Override public String usage() { return "addcolor <colorName> <roleId>"; }
    @Override public String category() { return "Staff"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.ROLE, BotConfig.STAFF_ROLE); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("addcolor", "addcolour", "addclr")); }

    @Override
    public void onCommand(CommandContext ctx) {
        String[] args = (String[]) ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        DiscordApi api = ctx.getApi();

        if (args.length != 2) {
            channel.sendMessage(new Warning("Insufficient arguments").sendWarning());
            return;
        }

        String colour = args[0];
        String id = args[1];

        Role role = api.getRoleById(args[1]).orElse(null);
        if (role == null) {
            channel.sendMessage(new Warning("Supplied roleId isn't a roleId").sendWarning());
            return;
        }

        RoleColourUtility.addColourRole(colour, id);

        channel.sendMessage(new EmbedBuilder()
                .setTitle(role.getName())
                .setColor(role.getColor().orElse(Color.GREEN))
                .addField("Role id:", role.getIdAsString())
                .addField("Colour to be stored as:", args[0])
        );
    }
}
