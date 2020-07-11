/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.developer;

import dev.salmonllama.fsbot.database.controllers.StaticPermissionController;
import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class PermissionCommand extends Command {
    @Override public String name() { return "Permission"; }
    @Override public String description() { return "Manages a user's static permissions"; }
    @Override public String usage() { return "permission <list|add|remove> <keyword>"; }
    @Override public String category() { return "Staff"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.OWNER); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("permission", "perm")); }

    @Override
    public void onCommand(CommandContext ctx) {
        String[] args = ctx.getArgs();

        switch(args[0]) {
            case "list":
                // List all the static permissions
                break;
            case "add":
                // Add a static permission to the mentioned user, if any
                break;
            case "remove":
                // Remove a static permission from the mentioned user, if any
                break;
            default:
                // You don't know how to use this command LUL
        }
    }

    private void list(CommandContext ctx) {

    }

    private void listForUser(CommandContext ctx) {

    }

    private void add(CommandContext ctx) {

    }

    private void remove(CommandContext ctx) {

    }
}
