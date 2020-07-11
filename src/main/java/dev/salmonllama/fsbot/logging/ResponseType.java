/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.logging;

import java.awt.*;

public enum ResponseType {
    ERROR (Color.RED),
    WARN (Color.YELLOW),
    INFO (Color.BLUE),
    LOG (Color.GRAY);

    private Color color;
    private String title;
    ResponseType(Color color, String title) {

    }
}
