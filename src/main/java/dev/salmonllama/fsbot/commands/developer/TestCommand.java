/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.developer;

import dev.salmonllama.fsbot.database.controllers.OutfitController;
import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class TestCommand extends Command {
    @Override public String name() { return "Test"; }
    @Override public String description() { return "A test command"; }
    @Override public String usage() { return "test"; }
    @Override public String category() { return "Developer"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.OWNER); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("test", "t")); }

    @Override
    public void onCommand(CommandContext ctx) {
        OutfitController.findRandom().thenAccept(outfit -> {
            outfit.ifPresent(o -> ctx.reply(o.toString()));
        });
    }
}
