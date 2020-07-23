/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot;

import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.database.FSDB;
import dev.salmonllama.fsbot.guthix.Guthix;
import dev.salmonllama.fsbot.listeners.*;
import org.javacord.api.DiscordApiBuilder;

import dev.salmonllama.fsbot.utilities.Constants;

// TODO: auto-switching status messages.
// TODO: Add an official Logger --> logging to Discord, not console

public class Main {

    public static void main(String[] args) {
        String configLocation = Constants.BOT_FOLDER.concat(Constants.CONFIG_NAME);
        BotConfig.initConfig(configLocation);

        FSDB.init();

        new DiscordApiBuilder().setToken(BotConfig.TOKEN).login().thenAccept(api -> {

            @SuppressWarnings("unused")
            Guthix guthix = new Guthix(api);

            // Register listeners
            api.addMessageCreateListener(new ImageListener());
            api.addServerMemberJoinListener(new NewMemberListener());
            api.addServerJoinListener(new ServerJoined(api));
            api.addMessageCreateListener(new ThumbsListener());
            api.addMessageCreateListener(new AchievementListener());
            api.addMessageCreateListener(new ReportListener());

            System.out.println(String.format("Bot invite: %s", api.createBotInvite()));
            System.out.println(String.format("Logged in as %s", api.getYourself().getDiscriminatedName()));
        });
    }
}
