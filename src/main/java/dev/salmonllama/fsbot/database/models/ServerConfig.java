package dev.salmonllama.fsbot.database.models;

import dev.salmonllama.fsbot.database.DatabaseModel;

public class ServerConfig extends DatabaseModel {
    private String id;
    private String name;
    private String prefix;

    public ServerConfig(ServerConfigBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.prefix = builder.prefix;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public static String schema() {
        return "CREATE TABLE IF NOT EXISTS server_config (" +
                "id TEXT PRIMARY KEY ," +
                "name TEXT," +
                "prefix TEXT)";
    }

    @Override
    public String toString() {
        return String.format("Server Config: [id: %s, name: %s, prefix: %s]",
                id, name, prefix
        );
    }

    public static class ServerConfigBuilder {
        private String id;
        private String name;
        private String prefix;

        public ServerConfigBuilder() {

        }

        public ServerConfigBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public ServerConfigBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ServerConfigBuilder setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public ServerConfig build() {
            return new ServerConfig(this);
        }
    }
}