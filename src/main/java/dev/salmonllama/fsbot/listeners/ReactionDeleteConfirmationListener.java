/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.listeners;

import com.vdurmont.emoji.EmojiParser;
import dev.salmonllama.fsbot.config.BotConfig;
import org.javacord.api.entity.channel.Channel;
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

public class ReactionDeleteConfirmationListener implements ReactionAddListener {

    private final User author;
    private final Message message;
    private final Outfit outfit;
    private final DatabaseUtilities db;

    public ReactionDeleteConfirmationListener(User author, Message message, Outfit outfit, DatabaseUtilities db) {
        this.author = author;
        this.message = message;
        this.outfit = outfit;
        this.db = db;
    }

    public void onReactionAdd(ReactionAddEvent event) {
        if (!event.getUser().getIdAsString().equals(author.getIdAsString())) {
            return;
        }

        if (event.getEmoji().equalsEmoji(EmojiParser.parseToUnicode(":white_check_mark:"))) {
            // Delete the message and send confirmation

            String completed = "";

            try {
                completed = db.removeFromDatabase(outfit.getId());
            }
            catch(OutfitNotFoundException e) {
                event.getChannel().sendMessage(new DiscordError(e.getMessage()).get().addField("Info:", "This message was sent by a reaction listener. **YOU SHOULD NOT BE SEEING THIS!**"));
            }

            if (Integer.parseInt(completed) > 0) {
                // Successful deletion
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Success!")
                        .setColor(Color.GREEN)
                        .setDescription("Outfit deleted successfully");

                message.removeAllReactions();
                message.edit(embed);

                event.getApi().getChannelById(BotConfig.OUTFIT_LOG).map(Channel::asServerTextChannel).orElseThrow(AssertionError::new).map(channel ->
                        channel.sendMessage(outfit.generateInfo().setTitle(String.format("Outfit Deleted by %s", event.getUser().getDiscriminatedName())).setColor(Color.RED))
                );
            }
            else {
                // Deletion failure
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Error!")
                        .setColor(Color.RED)
                        .setDescription("An error occurred and the outfit was not deleted. Did you use the correct ID?")
                        .setFooter(String.format("Bother %s about making these stupid things more useful.", event.getApi().getOwner().thenAccept(User::getDiscriminatedName)));

                message.removeAllReactions();
                message.edit(embed);
            }
        }
        else if (event.getEmoji().equalsEmoji(EmojiParser.parseToUnicode(":octagonal_sign:"))) {
            // Cancel deletion and do nothing
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Oops!")
                    .setColor(Color.GREEN)
                    .setDescription("Deletion cancelled. No changes were made.");

            message.removeAllReactions();
            message.edit(embed);
        }

        event.getApi().removeListener(this);
    }
}
