/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.guthix;

public class CommandPermission {
    private PermissionType type;
    private String value;

    public CommandPermission(PermissionType type) {
        this.type = type;
    }

    public CommandPermission(PermissionType type, String value) {
        this.type = type;
        this.value = value;
    }

    public PermissionType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
