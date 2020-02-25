/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.exceptions;

public class FailedUploadException extends Exception {
    private String message;

    public FailedUploadException(String imageLink) {
        message = "Failed to upload: ".concat(imageLink);
    }

    @Override
    public String toString() {
        return message;
    }
}
