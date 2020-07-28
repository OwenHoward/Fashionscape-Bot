/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.logging;

import java.awt.*;

public enum ResponseType {
    ERROR (Color.RED, "Error"),
    WARN (Color.YELLOW, "Warning"),
    INFO (Color.BLUE, "Info"),
    LOG (Color.GRAY, "Log");

    private Color color;
    private String title;
    ResponseType(Color color, String title) {

    }
}
