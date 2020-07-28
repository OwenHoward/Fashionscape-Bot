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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class AddColorCommand extends Command {
    @Override public String name() { return "Add Color"; }
    @Override public String description() { return "adds the provided role to the toggleable cosmetic roles."; }
    @Override public String usage() { return "addcolor <colorName> <roleId>"; }
    @Override public String category() { return "Staff"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.STATIC, "staff"); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("addcolor", "addcolour", "addclr")); }

    @Override
    public void onCommand(CommandContext ctx) {
        ctx.reply("This command is a WIP and will be available soon.");
    }
}
