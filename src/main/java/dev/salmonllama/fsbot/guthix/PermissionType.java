/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.guthix;

public enum PermissionType {
    ROLE,   // User has a specific role inside the server
    PERMISSION, // User has a specific Discord permission bit
    STATIC, // User has a pre-defined permission stored in the database
    OWNER,  // User is the bot owner
    NONE    // No requirement
}
