/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.database.models;

import dev.salmonllama.fsbot.database.DatabaseModel;

import java.sql.Timestamp;

public class WelcomeMessage extends DatabaseModel {
    // Need the message, the last time it was updated, and the id of the last person who updated it.
    private String serverId;
    private String message;
    private Timestamp updated;
    private String editor;

    private WelcomeMessage(WelcomeMessageBuilder builder) {
        serverId = builder.serverId;
        message = builder.message;
        updated = builder.updated;
        editor = builder.editor;
    }

    public String getServerId() {
        return serverId;
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

    public static String schema() {
        return "CREATE TABLE IF NOT EXISTS welcome_messages (" +
                "server_id TEXT PRIMARY KEY," +
                "message TEXT," +
                "updated TEXT," +
                "editor TEXT)";
    }

    public static class WelcomeMessageBuilder {
        private String serverId;
        private String message = null;
        private Timestamp updated = null;
        private String editor = null;

        public WelcomeMessageBuilder(String serverId) {
            this.serverId = serverId;
        }

        public WelcomeMessageBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public WelcomeMessageBuilder setUpdated(Timestamp updated) {
            this.updated = updated;
            return this;
        }

        public WelcomeMessageBuilder setEditor(String editor) {
            this.editor = editor;
            return this;
        }

        public WelcomeMessage build() {
            return new WelcomeMessage(this);
        }
    }
}
