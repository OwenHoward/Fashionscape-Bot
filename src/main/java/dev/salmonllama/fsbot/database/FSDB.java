/*
 Copyright (c) 2021 Aleksei Gryczewski

 Permission is hereby granted, free of charge, to any person obtaining a copy of
 this software and associated documentation files (the "Software"), to deal in
 the Software without restriction, including without limitation the rights to
 use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 the Software, and to permit persons to whom the Software is furnished to do so,
 subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

// Heavily inspired by Kaaz's Emily database connection: https://github.com/Kaaz/DiscordBot/tree/master/src/main/java/emily/db
package dev.salmonllama.fsbot.database;

import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.database.models.*;

import java.sql.SQLException;
import java.util.HashMap;

public class FSDB {
    private static final String DEFAULT_CONNECTION = "fsbot";
    private static final HashMap<String, DatabaseProvider> connections = new HashMap<>();

    public static DatabaseProvider get(String key) {
        if (connections.containsKey(key)) {
            return connections.get(key);
        }

        System.out.printf("Specified connection %s has not been set.%n", key);
        return null;
    }

    public static DatabaseProvider get() {
        return connections.get(DEFAULT_CONNECTION);
    }

    public static void init() {
        connections.clear();
        connections.put(DEFAULT_CONNECTION, new DatabaseProvider(BotConfig.DB_NAME));

        prepareTables();
    }

    private static void prepareTables() {
        try {
            get().query(Outfit.schema());
//            get().query(ColorRole.schema());
            get().query(GalleryChannel.schema());
            get().query(ServerConfig.schema());
            get().query(ServerBlacklist.schema());
            get().query(UserBlacklist.schema());
            get().query(StaticPermission.schema());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
