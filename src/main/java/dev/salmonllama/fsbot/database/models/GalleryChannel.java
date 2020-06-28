/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.database.models;

import dev.salmonllama.fsbot.database.DatabaseModel;

public class GalleryChannel extends DatabaseModel {
    public String serverId;
    public String serverName;
    public String channelId;
    public String channelName;
    public String tag;
    // Normal emojis will be stored in plain text as :emoji:
    // Server emojis will be stored in plain text as <:emoji:emoji-id>
    // This can be acquired through CustomEmoji#getMentionTag()
    public String emoji;

    public GalleryChannel() {

    }

    public String getServerId() {
        return serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getTag() {
        return tag;
    }

    public String getEmoji() {
        return emoji;
    }

    public static String schema() {
        return "CREATE TABLE IF NOT EXISTS galleries (" +
                "server_id TEXT," +
                "server_name TEXT," +
                "channel_id TEXT," + // TODO: PRIMARY KEY? There can only be one gallery per channel.
                "channel_name TEXT," +
                "emoji TEXT," +
                "tag TEXT)";
    }

    @Override
    public String toString() {
        return String.format("Gallery: [server id: %s, server name: %s, channel id: %s, channel name: %s, tag: %s, emoji: %s]",
                serverId, serverName, channelId, channelName, tag, emoji);
    }
}
