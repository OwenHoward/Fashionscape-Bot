/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

// Shoutout to Kaaz (again) for a kickass config service: https://github.com/Kaaz/ConfigurationBuilder
package dev.salmonllama.fsbot.config;

import com.kaaz.configuration.ConfigurationBuilder;
import com.kaaz.configuration.ConfigurationOption;

import java.io.File;

public class BotConfig {
    @ConfigurationOption
    public static String TOKEN = "token-goes-here";

    @ConfigurationOption
    public static String DB_ADDR = "fsbot.db";

    @ConfigurationOption
    public static String DB_NAME = "fsbot";

    @ConfigurationOption
    public static String DEFAULT_PREFIX = "~";

    @ConfigurationOption
    public static String BOT_OWNER = "owner's id here";

    @ConfigurationOption
    public static String STAFF_ROLE = "staff role id here";

    @ConfigurationOption
    public static String REPORT_CHANNEL = "report channel";

    @ConfigurationOption
    public static String ACHIEVEMENT_CHANNEL = "achievement channel here";

    @ConfigurationOption
    public static String ANNOUNCEMENT_CHANNEL = "announcement channel here";

    @ConfigurationOption
    public static String NEWS_CHANNEL = "news channel here";

    @ConfigurationOption
    public static String VOTE_CHANNEL = "vote channel here";

    @ConfigurationOption
    public static String WELCOME_CHANNEL = "welcome channel here";

    @ConfigurationOption
    public static String JOIN_LOG = "join log channel";

    @ConfigurationOption
    public static String REPORT_LOG = "report_log_channel";

    @ConfigurationOption
    public static String OUTFIT_LOG = "outfit log channel";

    @ConfigurationOption
    public static String BOT_LOG = "bot_log_channel";

    @ConfigurationOption
    public static String HYDRIX_ROLE = "hydrix role id here";

    @ConfigurationOption
    public static String IMGUR_ID = "imgur_id_here";

    @ConfigurationOption
    public static String IMGUR_BEARER = "imgur bearer here";

    @ConfigurationOption
    public static String DEFAULT_REACTION = ":heartpulse:";

    @ConfigurationOption
    public static String HOME_SERVER = "340511685024546816";

    public static void initConfig(String filePath, boolean cleanfile) {
        try {
            new ConfigurationBuilder(BotConfig.class, new File(filePath)).build(cleanfile);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
