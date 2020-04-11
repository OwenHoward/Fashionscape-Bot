/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.database.controllers;

import dev.salmonllama.fsbot.database.FSDB;
import dev.salmonllama.fsbot.database.models.ServerConfig;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class ServerConfigController {
    // insert
    // retrieve
    // update
    // No need for delete -> guilds should persist after leaving.

    public static CompletableFuture<Void> insert(ServerConfig config) {
        return CompletableFuture.runAsync(() -> {
            try {
                insertExec(config);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    public static CompletableFuture<Optional<ServerConfig>> getServerConfig(String serverId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getServerConfigExec(serverId);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    public static CompletableFuture<Void> update(ServerConfig config) {
        return CompletableFuture.runAsync(() -> {
            try {
                updateExec(config);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    private static void insertExec(ServerConfig config) throws SQLException {
        FSDB.get().insert("INSERT INTO server_config('id', 'name', 'prefix') VALUES (?, ?, ?)",
                config.getId(),
                config.getName(),
                config.getPrefix()
        );
    }

    private static Optional<ServerConfig> getServerConfigExec(String serverId) throws SQLException {
        ResultSet rs = FSDB.get().select("SELECT * FROM server_config WHERE id = ?");

        if (rs.next()) {
            ServerConfig config = mapObject(rs);
            FSDB.get().close(rs);
            return Optional.of(config);
        }

        FSDB.get().close(rs);
        return Optional.empty();
    }

    private static void updateExec(ServerConfig config) throws SQLException {
        FSDB.get().query("UPDATE server_config SET prefix = ?, name = ? WHERE id = ?",
                config.getPrefix(),
                config.getName(),
                config.getId()
                );
    }

    private static ServerConfig mapObject(ResultSet rs) throws SQLException {
        return new ServerConfig.ServerConfigBuilder()
                .setId(rs.getString("id"))
                .setName(rs.getString("name"))
                .setPrefix(rs.getString("prefix"))
                .build();
    }
}
