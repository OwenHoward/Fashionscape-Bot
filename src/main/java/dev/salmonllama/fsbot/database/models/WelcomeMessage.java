/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.database.models;

import dev.salmonllama.fsbot.database.DatabaseModel;

import java.sql.Timestamp;

public class WelcomeMessage extends DatabaseModel {
    // Need the message, the last time it was updated, and the id of the last person who updated it.
    private String message;
    private Timestamp updated;
    private String editor;

    private WelcomeMessage(WelcomeMessageBuilder builder) {
        message = builder.message;
        updated = builder.updated;
        editor = builder.editor;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public String getEditor() {
        return editor;
    }

    public static class WelcomeMessageBuilder {
        private String message;
        private Timestamp updated = null;
        private String editor = null;

        public WelcomeMessageBuilder(String message) {
            this.message = message;
        }

        public void setUpdated(Timestamp updated) {
            this.updated = updated;
        }

        public void setEditor(String editor) {
            this.editor = editor;
        }

        public WelcomeMessage build() {
            return new WelcomeMessage(this);
        }
    }
}
