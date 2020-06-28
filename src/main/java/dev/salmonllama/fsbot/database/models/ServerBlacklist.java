/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.database.models;

import dev.salmonllama.fsbot.database.DatabaseModel;

import java.sql.Timestamp;

public class ServerBlacklist extends DatabaseModel { // TODO: Add a reason?
    private String id;
    private String name;
    private String ownerId;
    private Timestamp added;

    private ServerBlacklist(ServerBlacklistBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.ownerId = builder.ownerId;
        this.added = builder.added;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Timestamp getAdded() {
        return added;
    }

    public static String schema() {
        return "CREATE TABLE IF NOT EXISTS blacklist_servers (" +
                "id TEXT PRIMARY KEY," +
                "name TEXT," +
                "owner_id TEXT," +
                "added TEXT)";
    }

    public static class ServerBlacklistBuilder {
        private String id;
        private String name;
        private String ownerId;
        private Timestamp added;

        public ServerBlacklistBuilder(String id) {
            this.id = id;
        }

        public ServerBlacklistBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ServerBlacklistBuilder setOwnerId(String ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public ServerBlacklistBuilder setAdded(Timestamp added) {
            this.added = added;
            return this;
        }

        public ServerBlacklist build() {
            return new ServerBlacklist(this);
        }
    }
}
