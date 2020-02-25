/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.database.controllers;

import dev.salmonllama.fsbot.database.FSDB;
import dev.salmonllama.fsbot.database.models.Outfit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class OutfitController {
    public static CompletableFuture<Void> insert(Outfit outfit) {
        return CompletableFuture.runAsync(() -> {
            try {
                insertExec(outfit);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    public static CompletableFuture<Optional<Outfit>> findById(String id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return findByIdExec(id);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    public static CompletableFuture<Outfit> findRandom() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return findRandomExec();
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    public static CompletableFuture<Optional<Outfit>> findRandomByTag(String tag) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return findRandomByTagExec(tag);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    public static CompletableFuture<Optional<Outfit>> findRandomBySubmitter(String submitterId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return findRandomBySubmitterExec(submitterId);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    public static CompletableFuture<Integer> countOutfits() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return countOutfitsExec();
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    public static CompletableFuture<Integer> countOutfitsBySubmitter(String submitterId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return countOutfitsBySubmitterExec(submitterId);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    private static void insertExec(Outfit outfit) throws SQLException {
        if (outfit.created == null) {
            outfit.created = new Timestamp(System.currentTimeMillis());
        }

        if (outfit.updated == null) {
            outfit.updated = new Timestamp(System.currentTimeMillis());
        }

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
    }

    private static Optional<Outfit> findByIdExec(String id) throws SQLException {
        ResultSet rs = FSDB.get().select("SELECT * FROM outfits WHERE id = ?", id);

        if (rs.next()) {
            Outfit outfit = mapObject(rs);
            FSDB.get().close(rs);
            return Optional.of(outfit);
        }

        FSDB.get().close(rs);
        return Optional.empty();
    }

    private static Outfit findRandomExec() throws SQLException {
        ResultSet rs = FSDB.get().select("SELECT * FROM outfits WHERE deleted = 0 ORDER BY random() LIMIT 1");

        Outfit outfit = new Outfit();
        if (rs.next()) {
            outfit = mapObject(rs);
        }
        FSDB.get().close(rs);
        
        return outfit;
    }

    private static Optional<Outfit> findRandomByTagExec(String tag) throws SQLException {
        ResultSet rs = FSDB.get().select("SELECT * FROM outfits WHERE tag = ? AND deleted = 0 ORDER BY random() LIMIT 1", tag);

        if (rs.next()) {
            Outfit outfit = mapObject(rs);
            FSDB.get().close(rs);
            return Optional.of(outfit);
        }

        FSDB.get().close(rs);
        return Optional.empty();
    }

    private static Optional<Outfit> findRandomBySubmitterExec(String submitterId) throws SQLException {
        ResultSet rs = FSDB.get().select("SELECT * FROM outfits WHERE submitter = ? AND deleted = 0 ORDER BY random() LIMIT 1", submitterId);

        if (rs.next()) {
            Outfit outfit = mapObject(rs);
            FSDB.get().close(rs);
            return Optional.of(outfit);
        }

        FSDB.get().close(rs);
        return Optional.empty();
    }

    private static int countOutfitsExec() throws SQLException {
        ResultSet rs = FSDB.get().select("SELECT COUNT(*) AS count FROM outfits WHERE deleted = 0");

        int count = 0;
        if (rs.next()) {
            count = rs.getInt("count");
        }
        FSDB.get().close(rs);

        return count;
    }

    private static int countOutfitsBySubmitterExec(String submitterId) throws SQLException {
        ResultSet rs = FSDB.get().select("SELECT COUNT(*) AS count FROM outfits WHERE submitter = ? AND deleted = 0", submitterId);

        int count = 0;
        if (rs.next()) {
            count = rs.getInt("count");
        }
        FSDB.get().close(rs);

        return count;
    }

    private static Outfit mapObject(ResultSet rs) throws SQLException {
        Outfit outfit = new Outfit();
        outfit.id = rs.getString("id");
        outfit.link = rs.getString("link");
        outfit.tag = rs.getString("tag");
        outfit.submitter = rs.getString("submitter");
        outfit.created = new Timestamp(rs.getLong("created"));
        outfit.updated = new Timestamp(rs.getLong("updated"));
        outfit.deleted = rs.getBoolean("deleted");
        outfit.featured = rs.getBoolean("featured");
        outfit.displayCount = rs.getInt("display_count");
        outfit.deletionHash = rs.getString("deletion_hash");
        return outfit;
    }
}
