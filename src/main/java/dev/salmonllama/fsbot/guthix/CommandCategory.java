/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.guthix;

public enum CommandCategory {
    DEVELOPER("Developer"),
    GENERAL("General"),
    OSRS_ITEM_SEARCH("Oldschool Item Search"),
    RS3_ITEM_SEARCH("Runescape Item Search"),
    STAFF("Staff");

    private final String category;

    CommandCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
