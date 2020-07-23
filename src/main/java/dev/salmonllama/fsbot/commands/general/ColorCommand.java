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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ColorCommand extends Command {
    @Override public String name() { return "Color"; }
    @Override public String description() { return "Assigns the provided cosmetic role"; }
    @Override public String usage() { return "color <colorName>"; }
    @Override public String category() { return "General"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.NONE); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("color", "colour")); }

    @Override
    public void onCommand(CommandContext ctx) {
        ctx.reply("This command is a WIP and will be available soon");
    }
}
