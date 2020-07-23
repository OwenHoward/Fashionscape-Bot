/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.guthix;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

import org.javacord.api.entity.message.MessageAuthor;

import dev.salmonllama.fsbot.database.controllers.StaticPermissionController;
import dev.salmonllama.fsbot.database.models.StaticPermission;

public class PermissionManager {

    public PermissionManager() {
    }

    public boolean hasPermission(CommandPermission reqPerm, CommandContext ctx) {
        PermissionType permType = reqPerm.getType();
        String permValue = reqPerm.getValue();

        switch (permType) {
            case NONE:
                return true;
            case ROLE:
                // If the author has the role, yes. Doesn't work in DM
                return roleHandler(permValue, ctx);
            case STATIC:
                return staticHandler(permValue, ctx);
            case PERMISSION:
                return permissionHandler(ctx);
            case OWNER:
                // If the author is the owner, yes.
                return ownerHandler(ctx);
            default:
                return false;
        }
    }

    private boolean roleHandler(String roleId, CommandContext ctx) {
        if (ctx.getUserRoles().isEmpty()) {
            ctx.reply("This command can only be used in a server");
            return false;
        }
        return ctx.getUserRoles().get().stream().anyMatch(role -> role.getIdAsString().equals(roleId));
    }

    private boolean ownerHandler(CommandContext ctx) {
        return ctx.isUserOwner();
    }

    private boolean permissionHandler(CommandContext ctx) {
        // Not implemented yet
        return false;
    }

    private boolean staticHandler(String staticPerm, CommandContext ctx) {
        AtomicBoolean ret = new AtomicBoolean(false);

        StaticPermissionController.getByUser(ctx.getAuthor().getIdAsString()).thenAccept(possiblePerms -> {
            possiblePerms.ifPresent(
                    staticPermissions -> ret.set(
                            staticPermissions.stream()
                                    .map(StaticPermission::getPermission)
                                    .anyMatch(staticPerm::equals)
                    )
            );
        });

        return ret.get();
    }

    boolean sourceIsValid(MessageAuthor author) {
        return !author.isBotUser() && !author.isWebhook();
    }
}
