/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.database.controllers;

import dev.salmonllama.fsbot.database.FSDB;
import dev.salmonllama.fsbot.database.models.OutfitModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OutfitController {
    public static void insert(OutfitModel outfit) {
        try {
            FSDB.get().insert("INSERT INTO outfits('id', 'link') VALUES (?, ?)", outfit.id, outfit.link);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static OutfitModel findById(String id) throws SQLException {
        OutfitModel outfit = null;
        try (ResultSet rs = FSDB.get().select("SELECT * FROM outfits WHERE id = ?", id)) {
            if (rs.next()) {
                outfit = mapObject(rs);
            }
            rs.getStatement().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outfit;
    }

    public static OutfitModel findRandom() throws SQLException {
        OutfitModel outfit = null;
        try (ResultSet rs = FSDB.get().select("SELECT * FROM outfits ORDERBY random() LIMIT 1")) {
            if (rs.next()) {
                outfit = mapObject(rs);
            }
            rs.getStatement().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outfit;
    }

    public static OutfitModel findRandomByTag(String tag) {
        OutfitModel outfit = null;
        try (ResultSet rs = FSDB.get().select("SELECT * FROM outfits WHERE tag = ? ORDERBY random() LIMIT 1", tag)) {
            if (rs.next()) {
                outfit = mapObject(rs);
            }
            rs.getStatement().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outfit;
    }

    public static OutfitModel findRandomBySubmitter(String submitterId) {
        OutfitModel outfit = null;
        try (ResultSet rs = FSDB.get().select("SELECT * FROM outfits WHERE submitter = ? ORDERBY random() LIMIT 1", submitterId)) {
            if (rs.next()) {
                outfit = mapObject(rs);
            }
            rs.getStatement().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outfit;
    }

    public static int countOutfits() {
        int count = 0;
        try (ResultSet rs = FSDB.get().select("SELECT COUNT(*) AS count FROM outfits")) {
            if (rs.next()) {
                count = rs.getInt("count");
            }
            rs.getStatement().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static int countOutfitsBySubmitter(String submitterId) {
        int count = 0;
        try (ResultSet rs = FSDB.get().select("SELECT COUNT(*) AS count FROM outfits WHERE submitter = ?", submitterId)) {
            if (rs.next()) {
                count = rs.getInt("count");
            }
            rs.getStatement().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private static OutfitModel mapObject(ResultSet rs) throws SQLException {
        return new OutfitModel.OutfitBuilder(
                rs.getString("link"),
                rs.getString("submitter"),
                rs.getString("tag")
        )
                .setId(rs.getString("id"))
                .setCreated(rs.getTimestamp("created"))
                .setUpdated(rs.getTimestamp("updated"))
                .setDeleted(rs.getBoolean("deleted"))
                .setFeatured(rs.getBoolean("featured"))
                .setDisplayCount(rs.getInt("display_count"))
                .setDeletionHash(rs.getString("deletion_hash"))
                .build();
    }
}
