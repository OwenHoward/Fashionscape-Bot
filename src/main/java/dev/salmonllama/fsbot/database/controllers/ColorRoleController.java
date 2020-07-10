package dev.salmonllama.fsbot.database.controllers;

import dev.salmonllama.fsbot.database.FSDB;
import dev.salmonllama.fsbot.database.models.ColorRole;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class ColorRoleController {
    // Need insert, get by color, exists by color, exists by role, get by server, count, and delete
    public CompletableFuture<Void> insert(ColorRole cr) {
        return CompletableFuture.runAsync(() -> {
            try {
                insertExec(cr);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<Boolean> exists(String color) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return existsExec(color);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<Boolean> exists(long roleId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return existsExec(roleId);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<Optional<ColorRole>> get(String color) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getExec(color);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<Optional<ColorRole>> get(long roleId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getExec(roleId);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<Integer> count(long serverId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return countExec(serverId);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<Void> delete(long roleId) {
        return CompletableFuture.runAsync(() -> {
            try {
                deleteExec(roleId);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<Void> delete(String color) {
        return CompletableFuture.runAsync(() -> {
            try {
                deleteExec(color);
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        });
    }

    private void insertExec(ColorRole cr) throws SQLException {
        FSDB.get().insert("INSERT INTO color_roles (role_id, server_id, color) VALUES (?, ?, ?)",
                cr.getRoleId(),
                cr.getServerId(),
                cr.getColor()
        );
    }

    private boolean existsExec(String color) throws SQLException {
        ResultSet rs = FSDB.get().select("SELECT EXISTS(SELECT 1 FROM color_roles WHERE color = ?) AS hmm", color);
        boolean exists = rs.getBoolean("hmm");

        FSDB.get().close(rs);
        return exists;
    }

    private boolean existsExec(long roleId) throws SQLException {
        ResultSet rs = FSDB.get().select("SELECT EXISTS(SELECT 1 FROM color_roles WHERE role_id = ?) AS hmm", roleId);
        boolean exists = rs.getBoolean("hmm");

        FSDB.get().close(rs);
        return exists;
    }

    private Optional<ColorRole> getExec(String color) throws SQLException {
        ResultSet rs = FSDB.get().select("SELECT * FROM color_roles WHERE color = ?", color);

        if (rs.next()) {
            ColorRole colorRole = mapObject(rs);
            FSDB.get().close(rs);
            return Optional.of(colorRole);
        }

        FSDB.get().close(rs);
        return Optional.empty();
    }

    private Optional<ColorRole> getExec(Long roleId) throws SQLException {
        ResultSet rs = FSDB.get().select("SELECT * FROM color_roles WHERE role_id = ?", roleId);

        if (rs.next()) {
            ColorRole colorRole = mapObject(rs);
            FSDB.get().close(rs);
            return Optional.of(colorRole);
        }

        FSDB.get().close(rs);
        return Optional.empty();
    }

    private int countExec(long serverId) throws SQLException {
        ResultSet rs = FSDB.get().select("SELECT COUNT(*) AS count FROM color_roles WHERE server_id = ?", serverId);

        if (rs.next()) {
            int count = rs.getInt("count");
            FSDB.get().close(rs);
            return count;
        }

        FSDB.get().close(rs);
        return 0;
    }

    private void deleteExec(long roleId) throws SQLException {
        FSDB.get().query("DELETE FROM color_roles WHERE role_id = ?", roleId);
    }

    private void deleteExec(String color) throws SQLException {
        FSDB.get().query("DELETE FROM color_roles WHERE color = ?", color);
    }

    private ColorRole mapObject(ResultSet rs) throws SQLException {
        return new ColorRole.ColorRoleBuilder(rs.getLong("role_id"))
                .setServerId(rs.getLong("server_id"))
                .setColor(rs.getString("color"))
                .build();
    }
}