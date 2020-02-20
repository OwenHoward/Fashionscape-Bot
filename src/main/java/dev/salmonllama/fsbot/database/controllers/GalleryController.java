/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.database.controllers;

import dev.salmonllama.fsbot.database.FSDB;
import dev.salmonllama.fsbot.database.models.GalleryChannel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class GalleryController {
    public static void insert(GalleryChannel gallery) throws SQLException {
        FSDB.get().insert("INSERT INTO galleries('server_id', 'server_name', 'channel_id', 'channel_name', 'tag')" +
                "VALUES(?, ?, ?, ?, ?)",
                gallery.serverId,
                gallery.serverName,
                gallery.channelId,
                gallery.channelName,
                gallery.tag
        );
    }

    public static Collection<GalleryChannel> getGalleriesByServer(String serverId) throws SQLException {
        ResultSet rs = FSDB.get().select("SELECT * FROM galleries WHERE server_id = ?", serverId);

        Collection<GalleryChannel> galleries = new ArrayList<>();
        while (rs.next()) {
            galleries.add(mapObject(rs));
        }

        return galleries;
    }

    private static GalleryChannel mapObject(ResultSet rs) throws SQLException {
        GalleryChannel gallery = new GalleryChannel();
        gallery.serverId = rs.getString("server_id");
        gallery.serverName = rs.getString("server_name");
        gallery.channelId = rs.getString("channel_id");
        gallery.channelName = rs.getString("channel_name");
        gallery.tag = rs.getString("tag");

        return gallery;
    }
}
