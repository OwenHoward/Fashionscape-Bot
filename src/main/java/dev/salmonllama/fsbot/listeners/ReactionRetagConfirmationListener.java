/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.listeners;

import com.vdurmont.emoji.EmojiParser;
import dev.salmonllama.fsbot.config.BotConfig;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import dev.salmonllama.fsbot.utilities.Outfit;
import dev.salmonllama.fsbot.utilities.database.DatabaseUtilities;
import dev.salmonllama.fsbot.utilities.exceptions.DiscordError;
import dev.salmonllama.fsbot.utilities.exceptions.OutfitNotFoundException;

import java.awt.*;

public class ReactionRetagConfirmationListener implements ReactionAddListener {

    private User author;
    private final Message message;
    private final Outfit outfit;
    private final DatabaseUtilities db;
    private final String oldTag;
    private final String newTag;

    public ReactionRetagConfirmationListener(User author, Message message, Outfit outfit, DatabaseUtilities db, String oldTag, String newTag) {
        this.author = author;
        this.message = message;
        this.outfit = outfit;
        this.db = db;
        this.oldTag = oldTag;
        this.newTag = newTag;
    }

    public void onReactionAdd(ReactionAddEvent event) {
        if (!event.getUser().getIdAsString().equals(this.author.getIdAsString())) {
            return;
        }

        if (event.getEmoji().equalsEmoji(EmojiParser.parseToUnicode(":white_check_mark:"))) {
            // retag the image, send a confirmation, or an error if it failed for whatever reason.
            String result = "";

            try {
                result = db.changeOutfitTag(outfit.getId(), this.newTag);
            }
            catch (OutfitNotFoundException e) {
                message.delete();
                event.getChannel().sendMessage(new DiscordError(e.getMessage()).get().addField("Info", "This message was generated by a listener thread. YOU SHOULD NOT BE SEEING THIS ERROR!"));
            }

            message.removeAllReactions();

            if (Integer.parseInt(result) > 0) {
                // success, send confirmation
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Success")
                        .setColor(Color.GREEN)
                        .setDescription("Outfit retagged successfully")
                        .setFooter("Check the log for more information");

                message.edit(embed);

                // log message with new tag and old tag

                event.getApi().getChannelById(BotConfig.OUTFIT_LOG).ifPresent(channel -> {
                    channel.asServerTextChannel().ifPresent(chnl -> {
                        chnl.sendMessage(outfit.tagChangeEmbed(this.author.getDiscriminatedName(), this.oldTag, this.newTag));
                    });
                });
            }
            else {
                // failure, something went wrong
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Error!")
                        .setColor(Color.RED)
                        .setFooter("Big oopsie");

                event.getApi().getOwner().thenAcceptAsync(user -> {
                    embed.setDescription(String.format("Something has gone horribly wrong, contact %s", user.getDiscriminatedName()));
                });

                message.edit(embed);
            }
        }
        else if (event.getEmoji().equalsEmoji(EmojiParser.parseToUnicode(":octagonal_sign:"))) {
            // Cancel the image retagging.

            message.removeAllReactions();

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Operation cancelled!")
                    .setDescription("No changes made.")
                    .setColor(Color.GREEN)
                    .setFooter("boop");

            message.edit(embed);
        }

        event.getApi().removeListener(this);
    }
}
