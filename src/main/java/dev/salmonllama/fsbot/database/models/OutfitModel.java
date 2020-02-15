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

    public OutfitModel() {

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
}
