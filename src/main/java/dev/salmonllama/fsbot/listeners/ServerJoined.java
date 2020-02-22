/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.listeners;

import dev.salmonllama.fsbot.utilities.database.DatabaseUtilities;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.server.ServerJoinEvent;
import org.javacord.api.listener.server.ServerJoinListener;

import java.awt.*;

public class ServerJoined implements ServerJoinListener {

    private DiscordApi api;
    private DatabaseUtilities db;

    public ServerJoined(DiscordApi api, DatabaseUtilities db) {
        this.api = api;
        this.db = db;
    }

    public void onServerJoin(ServerJoinEvent event) { // TODO: This needs fixing yo
    //     db.newServerProcess(event.getServer());

    //     EmbedBuilder embed = new EmbedBuilder()
    //             .setTitle("Server joined")
    //             .setColor(Color.GREEN)
    //             .addInlineField("Server name:", event.getServer().getName())
    //             .addInlineField("Server Id:", event.getServer().getIdAsString());
    }
}
