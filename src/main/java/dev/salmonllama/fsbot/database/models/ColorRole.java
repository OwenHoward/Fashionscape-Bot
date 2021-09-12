/*
 * Copyright (c) 2021 Aleksei Gryczewski
 */

package dev.salmonllama.fsbot.database.models;

import dev.salmonllama.fsbot.database.DatabaseModel;

public class ColorRole extends DatabaseModel {
    private final long roleId;
    private final long serverId;
    private final String color;

    public ColorRole(ColorRoleBuilder builder) {
        roleId = builder.roleId;
        serverId = builder.serverId;
        color = builder.color;
    }

    public long getRoleId() {
        return roleId;
    }

    public long getServerId() {
        return serverId;
    }

    public String getColor() {
        return color;
    }

    public static String schema() {
        return "CREATE TABLE IF NOT EXISTS color_roles (role_id INTEGER, server_id INTEGER, color TEXT)";
    }

    @Override
    public String toString() {
        return String.format("Color Role: {roleId: %d, serverId: %d, color: %s", roleId, serverId, color);
    }

    public static class ColorRoleBuilder {
        private final long roleId;
        private long serverId;
        private String color;

        public ColorRoleBuilder(long roleId) {
            this.roleId = roleId;
        }

        public ColorRoleBuilder setServerId(long serverId) {
            this.serverId = serverId;
            return this;
        }

        public ColorRoleBuilder setColor(String color) {
            this.color = color;
            return this;
        }

        public ColorRole build() {
            return new ColorRole(this);
        }
    }
}
