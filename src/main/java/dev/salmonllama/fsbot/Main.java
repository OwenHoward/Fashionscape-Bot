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

package dev.salmonllama.fsbot;

import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.database.FSDB;
import dev.salmonllama.fsbot.guthix.Guthix;
import dev.salmonllama.fsbot.listeners.*;
import org.javacord.api.DiscordApiBuilder;

import dev.salmonllama.fsbot.utilities.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Main {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        BotConfig.initConfig(Constants.BOT_FOLDER, false);

        FSDB.init();

        new DiscordApiBuilder().setToken(BotConfig.TOKEN).login().thenAccept(api -> {

            @SuppressWarnings("unused")
            Guthix guthix = new Guthix(api);

            // Register listeners
            api.addMessageCreateListener(new ImageListener());
            api.addServerMemberJoinListener(new NewMemberListener());
            api.addServerJoinListener(new ServerJoined());
            api.addMessageCreateListener(new ThumbsListener());
            api.addMessageCreateListener(new AchievementListener());
            api.addMessageCreateListener(new ReportListener());

            logger.info("{} reporting for duty", api.getYourself().getDiscriminatedName());
        });

        SpringApplication.run(Main.class, args);
    }
}
