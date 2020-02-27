/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.guthix;

import org.javacord.api.entity.message.MessageAuthor;

public class PermissionManager {

    public PermissionManager() {
    }

    public boolean hasPermission(CommandPermission reqPerm, CommandContext ctx) {
        PermissionType permtype = reqPerm.getType();

        switch (permtype) {
            case NONE:
                return true;
            case ROLE:
                // If the author has the role, yes. Doesn't work in DM
                return roleHandler(reqPerm.getValue(), ctx);
            case OWNER:
                // If the author is the owner, yes.
                return ownerHandler(ctx);
        }

        return false;
    }

    private boolean roleHandler(String roleId, CommandContext ctx) {
        if (!ctx.getUserRoles().isPresent()) {
            ctx.reply("This command can only be used in a server");
            return false;
        }
        return ctx.getUserRoles().get().stream().anyMatch(role -> role.getIdAsString().equals(roleId));
    }

    private boolean ownerHandler(CommandContext ctx) {
        return ctx.isUserOwner();
    }

    boolean sourceIsValid(MessageAuthor author) {
        return !author.isBotUser() && !author.isWebhook();
    }
}
