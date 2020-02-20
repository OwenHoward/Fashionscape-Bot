/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.database.controllers;

import dev.salmonllama.fsbot.database.FSDB;
import dev.salmonllama.fsbot.database.models.Outfit;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OutfitController {
    public static void insert(Outfit outfit) {
        try {
            FSDB.get().insert(
                    "INSERT INTO " +
                            "outfits('id', 'link', 'submitter', 'tag', 'created', 'updated', 'deleted', 'featured', 'display_count', 'deletion_hash') " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    outfit.id,
                    outfit.link,
                    outfit.submitter,
                    outfit.tag,
                    outfit.created,
                    outfit.updated,
                    outfit.deleted,
                    outfit.featured,
                    outfit.displayCount,
                    outfit.deletionHash
                    );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Outfit findById(String id) throws SQLException {
        Outfit outfit = new Outfit();
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

    public static Outfit findRandom() throws SQLException {
        Outfit outfit = new Outfit();
        try (ResultSet rs = FSDB.get().select("SELECT * FROM outfits WHERE deleted = 0 ORDERBY random() LIMIT 1")) {
            if (rs.next()) {
                outfit = mapObject(rs);
            }
            rs.getStatement().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outfit;
    }

    public static Outfit findRandomByTag(String tag) {
        Outfit outfit = new Outfit();
        try (ResultSet rs = FSDB.get().select("SELECT * FROM outfits WHERE tag = ? AND deleted = 0 ORDERBY random() LIMIT 1", tag)) {
            if (rs.next()) {
                outfit = mapObject(rs);
            }
            rs.getStatement().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outfit;
    }

    public static Outfit findRandomBySubmitter(String submitterId) {
        Outfit outfit = new Outfit();
        try (ResultSet rs = FSDB.get().select("SELECT * FROM outfits WHERE submitter = ? AND deleted = 0 ORDERBY random() LIMIT 1", submitterId)) {
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
        try (ResultSet rs = FSDB.get().select("SELECT COUNT(*) AS count FROM outfits WHERE deleted = 0")) {
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
        try (ResultSet rs = FSDB.get().select("SELECT COUNT(*) AS count FROM outfits WHERE submitter = ? AND deleted = 0", submitterId)) {
            if (rs.next()) {
                count = rs.getInt("count");
            }
            rs.getStatement().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private static Outfit mapObject(ResultSet rs) throws SQLException {
        Outfit outfit = new Outfit();
        outfit.id = rs.getString("id");
        outfit.link = rs.getString("link");
        outfit.tag = rs.getString("tag");
        outfit.submitter = rs.getString("submitter");
        outfit.created = rs.getTimestamp("created");
        outfit.updated = rs.getTimestamp("updated");
        outfit.deleted = rs.getBoolean("deleted");
        outfit.featured = rs.getBoolean("featured");
        outfit.displayCount = rs.getInt("display_count");
        outfit.deletionHash = rs.getString("deletion_hash");
        return outfit;
    }
}
