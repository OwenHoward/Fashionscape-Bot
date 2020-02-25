/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.database.models;

import dev.salmonllama.fsbot.database.DatabaseModel;

import java.sql.Timestamp;

public class Outfit extends DatabaseModel {
    private String id = "";
    private String link = "";
    private String submitter = "";
    private String tag = "";
    private Timestamp created = null;
    private Timestamp updated = null;
    private boolean deleted = false;
    private boolean featured = false;
    private int displayCount = 0;
    private String deleteHash = "";

    public Outfit(OutfitBuilder builder) {
        id = builder.id;
        link = builder.link;
        submitter = builder.submitter;
        tag = builder.tag;
        created = builder.created;
        updated = builder.updated;
        deleted = builder.deleted;
        featured = builder.featured;
        displayCount = builder.displayCount;
        deleteHash = builder.deleteHash;
    }

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getSubmitter() {
        return submitter;
    }

    public String getTag() {
        return tag;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp c) {
        this.created = c;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp u) {
        this.updated = u;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public boolean isFeatured() {
        return featured;
    }

    public int getDisplayCount() {
        return displayCount;
    }

    public String getDeleteHash() {
        return deleteHash;
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
                "display_count INT," +
                "delete_hash TEXT)";
    }

    @Override
    public String toString() {
        return String.format("Outfit: [id: %s, link: %s, submitter: %s, tag: %s, created: %s, updated: %s, deleted: %s, featured: %s, display count: %s, deletion hash: %s]",
                id, link, submitter, tag, created, updated, deleted, featured, displayCount, deleteHash
        );
    }

    public static class OutfitBuilder {
        private String id;
        private String link;
        private String submitter;
        private String tag;
        private Timestamp created = null;
        private Timestamp updated = null;
        private boolean deleted = false;
        private boolean featured = false;
        private int displayCount = 0;
        private String deleteHash = "";

        public OutfitBuilder() {
        }

        public OutfitBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public OutfitBuilder setLink(String link) {
            this.link = link;
            return this;
        }

        public OutfitBuilder setSubmitter(String submitter) {
            this.submitter = submitter;
            return this;
        }

        public OutfitBuilder setTag(String tag) {
            this.tag = tag;
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

        public OutfitBuilder setDeleteHash(String deletionHash) {
            this.deleteHash = deletionHash;
            return this;
        }

        public Outfit build() {
            return new Outfit(this);
        }
    }
}
