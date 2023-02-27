/*
 * Copyright (c) 2021 Aleksei Gryczewski
 */

package dev.salmonllama.fsbot.commands.developer;

import dev.salmonllama.fsbot.database.controllers.StaticPermissionController;
import dev.salmonllama.fsbot.database.models.StaticPermission;
import dev.salmonllama.fsbot.guthix.*;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.logging.ExceptionLogger;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class PermissionCommand extends Command {
    @Override public String name() { return "Permission"; }
    @Override public String description() { return "Manages a user's static permissions"; }
    @Override public String usage() { return "permission <list|add|remove> <@usermention> <keyword>"; }
    @Override public CommandCategory category() { return CommandCategory.DEVELOPER; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.OWNER); }
    @Override public List<String> aliases() { return Arrays.asList("permission", "permissions", "perm", "perms"); }

    @Override
    public void onCommand(CommandContext ctx) {
        String[] args = ctx.getArgs();

        switch (args[0]) {
            case "list" ->
                // List all the static permissions
                    list(ctx);
            case "add" ->
                // Add a static permission to the mentioned user, if any
                    add(ctx);
            case "remove" ->
                // Remove a static permission from the mentioned user, if any
                    remove(ctx);
            default -> ctx.reply("Incorrect usage, check ~help permission");
        }
    }

    private void list(CommandContext ctx) {
        if (!ctx.getMessage().getMentionedUsers().isEmpty()) {
            User mentionedUser = ctx.getMessage().getMentionedUsers().get(0);

            StaticPermissionController.getByUser(mentionedUser.getIdAsString()).thenAcceptAsync(possiblePerms -> {
                possiblePerms.ifPresentOrElse(perms -> {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle(String.format("Permissions for %s", mentionedUser.getName()))
                            .setFooter(String.format("User has %s total static permissions", perms.size()))
                            .setColor(Color.GREEN);

                    perms.forEach(perm -> {
                        embed.addField(perm.getUserId(), perm.getPermission());
                    });

                    ctx.reply(embed);
                }, () -> {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("No Permissions Found")
                            .setDescription(String.format("User %s has no static permissions", mentionedUser.getName()));

                    ctx.reply(embed);
                });
            });
        } else {
            StaticPermissionController.getAll().thenAcceptAsync(possiblePerms -> {
                possiblePerms.ifPresentOrElse(perms -> {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("All static permissions")
                            .setFooter(String.format("Found %s total static permissions", perms.size()))
                            .setColor(Color.green);

                    perms.forEach(perm -> {
                        embed.addField(perm.getUserId(), perm.getPermission());
                    });

                    ctx.reply(embed);
                }, () -> {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("No Permissions Found")
                            .setDescription("No permissions have been added.");

                    ctx.reply(embed);
                });
            });
        }
    }

    private void add(CommandContext ctx) {
        if (ctx.getMessage().getMentionedUsers().isEmpty()) {
            System.out.println("No mentioned users");
            // If no mentioned users, improper usage
            return;
        }

        String userId = ctx.getMessage().getMentionedUsers().get(0).getIdAsString();

        StaticPermission perm = new StaticPermission.StaticPermissionBuilder(userId).setPermission(ctx.getArgs()[2]).build();

        StaticPermissionController.insert(perm).thenAcceptAsync((Void) -> {
            EmbedBuilder response = new EmbedBuilder()
                    .setTitle("Permissions Added")
                    .addField("User:", userId)
                    .addField("Permission:", ctx.getArgs()[2]);

            ctx.reply(response);
        });
    }

    private void remove(CommandContext ctx) { // TODO: Remove is not functional
        if (ctx.getMessage().getMentionedUsers().isEmpty()) {
            // If no mentioned users, improper usage
            return;
        }

        String userId = ctx.getMessage().getMentionedUsers().get(0).getIdAsString();

        StaticPermissionController.delete(userId, ctx.getArgs()[2]).thenAcceptAsync((Void) -> {
            System.out.println("Permission removed");
        }).exceptionally(ExceptionLogger.get());
    }
}
