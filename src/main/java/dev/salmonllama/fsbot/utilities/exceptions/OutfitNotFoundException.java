/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.utilities.exceptions;

public class OutfitNotFoundException extends Exception {

    @Override
    public String getMessage() {
        return "An outfit with that ID was not found";
    }
}
