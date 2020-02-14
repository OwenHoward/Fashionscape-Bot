/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.utilities.exceptions;

import java.awt.*;

public class DiscordException {

    private Color color;
    private String message;
    private String footer;
    private String title;

    DiscordException(String title, String message, String footer, Color color) {
        this.title = title;
        this.message = message;
        this.footer = footer;
        this.color = color;
    }
}
