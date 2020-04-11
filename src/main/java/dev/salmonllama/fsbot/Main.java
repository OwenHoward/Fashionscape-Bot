/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.database.FSDB;
import dev.salmonllama.fsbot.guthix.Guthix;
import dev.salmonllama.fsbot.listeners.*;
import org.javacord.api.DiscordApiBuilder;

import dev.salmonllama.fsbot.utilities.Constants;
import dev.salmonllama.fsbot.utilities.database.DatabaseUtilities;

// TODO: auto-switching status messages.
// TODO: Add an official Logger --> logging to Discord, not console

public class Main {

    public static void main(String[] args) {
        String configLocation = Constants.BOT_FOLDER.concat(Constants.CONFIG_NAME);
        BotConfig.initConfig(configLocation);

        FSDB.init();

        // Initialise the database with values from the bot's config file
        RethinkDB r = RethinkDB.r;
        Connection conn = r.connection().hostname("localhost").port(28015).connect();

        new DiscordApiBuilder().setToken(BotConfig.TOKEN).login().thenAccept(api -> {
            DatabaseUtilities db = new DatabaseUtilities(r, conn, api);

            @SuppressWarnings("unused")
            Guthix guthix = new Guthix(api, db);

            // Register listeners
            api.addMessageCreateListener(new ImageListener(r, conn));
            api.addServerMemberJoinListener(new NewMemberListener(api));
            api.addServerJoinListener(new ServerJoined(api, db));
            api.addMessageCreateListener(new ThumbsListener());
            api.addMessageCreateListener(new AchievementListener());
            api.addMessageCreateListener(new ReportListener());

            System.out.println(String.format("Bot invite: %s", api.createBotInvite()));
            System.out.println(String.format("Logged in as %s", api.getYourself().getDiscriminatedName()));
        });
    }
}
