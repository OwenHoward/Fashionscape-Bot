/*
 * Copyright (c) 2021 Aleksei Gryczewski
 */

// Shoutout to Kaaz (again) for a kickass config service: https://github.com/Kaaz/ConfigurationBuilder
package dev.salmonllama.fsbot.config;

import com.kaaz.configuration.ConfigurationBuilder;
import com.kaaz.configuration.ConfigurationOption;
import dev.salmonllama.fsbot.utilities.Constants;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    public static String ACTIVITY_LOG = "bot_log_channel";

    @ConfigurationOption
    public static String HYDRIX_ROLE = "hydrix role id here";

    @ConfigurationOption
    public static String MEMBER_ROLE = "member role id here";

    @ConfigurationOption
    public static String IMGUR_ID = "imgur_id_here";

    @ConfigurationOption
    public static String IMGUR_BEARER = "imgur bearer here";

    @ConfigurationOption
    public static String DEFAULT_REACTION = ":heartpulse:";

    @ConfigurationOption
    public static String HOME_SERVER = "Home server here";

    public static void initConfig(Path filePath, boolean cleanfile) {
        try {
            new ConfigurationBuilder(BotConfig.class, Paths.get(filePath.toString(), Constants.CONFIG_NAME) .toFile()).build(cleanfile);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
