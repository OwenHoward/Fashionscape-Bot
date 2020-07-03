/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.database.controllers;

import dev.salmonllama.fsbot.database.FSDB;
import dev.salmonllama.fsbot.database.models.WelcomeMessage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class WelcomeMessageController {
    // Needs insert, update, and delete. Only one per server -> needs exists
    public CompletableFuture<Void> insert(WelcomeMessage msg) {
        return CompletableFuture.runAsync(() -> {
            try {
                insertExec(msg);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<Optional<WelcomeMessage>> get(String id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getExec(id);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<Void> update(WelcomeMessage msg) {
        return CompletableFuture.runAsync(() -> {
            try {
                updateExec(msg);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<Boolean> exists(String id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return existsExec(id);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<Void> delete(String id) {
        return CompletableFuture.runAsync(() -> {
            try {
                deleteExec(id);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<Void> delete(WelcomeMessage msg) {
        return CompletableFuture.runAsync(() -> {
            try {
                deleteExec(msg.getServerId());
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    private void insertExec(WelcomeMessage msg) throws SQLException {
        FSDB.get().insert("INSERT INTO welcome_messages (server_id, message, updated, editor) VALUES (?, ?, ?, ?)",
                msg.getServerId(),
                msg.getMessage(),
                msg.getUpdated(),
                msg.getEditor()
        );
    }

    private Optional<WelcomeMessage> getExec(String id) throws SQLException {
        ResultSet rs = FSDB.get().select("SELECT * FROM welcome_messages WHERE server_id = ?", id);

        if (rs.next()) {
            WelcomeMessage msg = mapObject(rs);
            FSDB.get().close(rs);
            return Optional.of(msg);
        }

        FSDB.get().close(rs);
        return Optional.empty();
    }
    
    private void updateExec(WelcomeMessage msg) throws SQLException {
        FSDB.get().query("UPDATE welcome_messages SET message = ?, updated = ?, editor = ? WHERE server_id = ?",
                msg.getMessage(),
                msg.getUpdated(),
                msg.getEditor(),
                msg.getServerId()
        );
    }

    private boolean existsExec(String id) throws SQLException {
        ResultSet rs = FSDB.get().select("SELECT EXISTS(SELECT 1 FROM welcome_messages WHERE server_id = ?) AS hmm", id);
        boolean exists = rs.getBoolean("hmm");

        FSDB.get().close(rs);
        return exists;
    }

    private void deleteExec(String id) throws SQLException {
        FSDB.get().query("DELETE FROM welcome_messages WHERE server_id = ?", id);
    }

    private WelcomeMessage mapObject(ResultSet rs) throws SQLException {
        return new WelcomeMessage.WelcomeMessageBuilder(rs.getString("server_id"))
                .setMessage(rs.getString("message"))
                .setUpdated(new Timestamp(rs.getLong("updated")))
                .setEditor(rs.getString("editor"))
                .build();
    }
}
