/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.staff;

import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.database.controllers.ColorRoleController;
import dev.salmonllama.fsbot.database.models.ColorRole;
import dev.salmonllama.fsbot.guthix.*;
import org.javacord.api.entity.permission.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class AddColorCommand extends Command {
    @Override public String name() { return "Add Color"; }
    @Override public String description() { return "adds the provided role to the toggleable cosmetic roles."; }
    @Override public String usage() { return "addcolor <colorName> <roleId>"; }
    @Override public CommandCategory category() { return CommandCategory.STAFF; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.STATIC, "staff"); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("addcolor", "addcolour", "addclr")); }

    @Override
    public void onCommand(CommandContext ctx) {
        // Command takes only a role mention.
        ctx.getServer().ifPresentOrElse(server -> {
            if (server.getIdAsString().equals(BotConfig.HOME_SERVER)) {
                List<Role> roles = ctx.getMessage().getMentionedRoles();
                roles.forEach(role -> {
                    ColorRole colorRole = new ColorRole.ColorRoleBuilder(role.getId())
                            .setColor(role.getName())
                            .setServerId(server.getId())
                            .build();

                    ColorRoleController.insert(colorRole);
                    ctx.reply("Added color role:" + colorRole.toString());
                });
            } else {
                ctx.reply("This command can only be used in the fashionscape server");
            }
        }, () -> ctx.reply("This command can only be used in the fashionscape server"));
    }
}
