/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.database.models;

import dev.salmonllama.fsbot.database.DatabaseModel;

import java.sql.Timestamp;

public class OutfitModel extends DatabaseModel {
    public String id = "";
    public String link = "";
    public String submitter = "";
    public String tag = "";
    public Timestamp created = null;
    public Timestamp updated = null;
    public boolean deleted = false;
    public boolean featured = false;
    public int displayCount = 0;
    public String deletionHash = "";

    private OutfitModel(OutfitBuilder builder) {
        id = builder.id;
        link = builder.link;
        submitter = builder.submitter;
        tag = builder.tag;
        created = builder.created;
        updated = builder.updated;
        deleted = builder.deleted;
        deletionHash = builder.deletionHash;
    }

    public static String schema() {
        return "CREATE TABLE IF NOT EXISTS outfits (" +
                "id TEXT, " +
                "link TEXT," +
                "submitter TEXT," +
                "tag TEXT," +
                "created TEXT," +
                "updated TEXT," +
                "deleted TEXT," +
                "featured TEXT," +
                "display_count INT" +
                "deletion_hash TEXT)";
    }

    public static class OutfitBuilder {
        public String id = "";
        public final String link;
        public final String submitter;
        public String tag;
        public Timestamp created = null;
        public Timestamp updated = null;
        public boolean deleted = false;
        public boolean featured = false;
        public int displayCount = 0;
        public String deletionHash = "";

        public OutfitBuilder(String link, String submitter, String tag) {
            this.link = link;
            this.submitter = submitter;
            this.tag = tag;
        }

        public OutfitBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public OutfitBuilder setCreated(Timestamp created) {
            this.created = created;
            return this;
        }

        public OutfitBuilder setUpdated(Timestamp updated) {
            this.updated = updated;
            return this;
        }

        public OutfitBuilder setDeleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public OutfitBuilder setFeatured(boolean featured) {
            this.featured = featured;
            return this;
        }

        public OutfitBuilder setDisplayCount(int displayCount) {
            this.displayCount = displayCount;
            return this;
        }

        public OutfitBuilder setDeletionHash(String hash) {
            this.deletionHash = hash;
            return this;
        }

        public OutfitModel build() {
            return new OutfitModel(this);
        }
    }
}
