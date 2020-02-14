/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.guthix;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.MessageAuthor;

public class PermissionManager {
    private DiscordApi api;

    public PermissionManager(DiscordApi api) {
        this.api = api;
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
        return ctx.getUserRoles().stream().anyMatch(role -> role.getIdAsString().equals(roleId));
    }

    private boolean ownerHandler(CommandContext ctx) {
        return ctx.isUserOwner();
    }

    boolean sourceIsValid(MessageAuthor author) {
        return !author.isBotUser() && !author.isWebhook();
    }
}
