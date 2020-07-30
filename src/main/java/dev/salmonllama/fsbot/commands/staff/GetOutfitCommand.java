/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.staff;

import dev.salmonllama.fsbot.database.controllers.OutfitController;
import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class GetOutfitCommand extends Command { // TODO: This command also needs HELP
    @Override public String name() { return "Get Outift"; }
    @Override public String description() { return "Shows the outfit, given an ID"; }
    @Override public String usage() { return "getoutfit <String id>"; }
    @Override public String category() { return "Staff"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.STATIC, "staff"); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("getoutfit", "get")); }

    @Override
    public void onCommand(CommandContext ctx) {
        // args is ONLY the ID
        String[] args = ctx.getArgs();

        String id = args[0];
        OutfitController.findById(id).thenAccept(outfitOpt -> {
            outfitOpt.ifPresentOrElse(
                outfit -> ctx.reply(outfit.toString()),
                () -> ctx.reply("Outfit not found, did you get the id right?")
            );
        });
    }
}
