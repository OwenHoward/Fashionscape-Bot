/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.staff;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class GetOutfitCommand extends Command { // TODO: This command also needs HELP
    @Override public String name() { return "Get Outift"; }
    @Override public String description() { return "Shows the outfit, given an ID"; }
    @Override public String usage() { return "getoutfit <String id>"; }
    @Override public String category() { return "Staff"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.ROLE, BotConfig.STAFF_ROLE); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("getoutfit", "get")); }

    static final RethinkDB r = RethinkDB.r;
    static final Connection conn = r.connection().hostname("localhost").port(28015).connect();

    @Override
    public void onCommand(CommandContext ctx) {
        String[] args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();

        if (args.length != 1) {
            channel.sendMessage("you did that wrong mate");
            return;
        }

        String id = args[0];

        // Check if the given id exists in the table
        try {
            r.db("fsbot").table("outfits").filter(row -> row.getField("id").eq(id)).run(conn);
        }
        catch (Exception e) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(Color.RED)
                    .addField("ERROR:", "That id does not exist in that table")
                    .addField("Output:", e.toString())
                    .setFooter("Contact Salmonllama#5727 if you see this");
            channel.sendMessage(embed);
            return;
        }

        // Get the entry for the given table/tag and id, build the embed, and send it.
        try {

            String embedLink = null;
            String embedSubmitter = null;

            Cursor linkCursor = r.db("fsbot")
                    .table("outfits")
                    .filter(row -> row.getField("id").eq(id))
                    .getField("link")
                    .run(conn);

            Cursor submitterCursor = r.db("fsbot")
                    .table("outfits")
                    .filter(row -> row.getField("id").eq(id))
                    .getField("submitter")
                    .run(conn);

            List links = linkCursor.toList();
            for (Object link : links) {
                embedLink = link.toString();
            }

            List submitters = submitterCursor.toList();
            for (Object submitter : submitters) {
                embedSubmitter = submitter.toString();
            }

            // Send a diff message if any of the things are null
            if (embedLink == null) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setColor(Color.RED)
                        .addField("ERROR: ", "The entry you tried to access doesn't exist.");
                channel.sendMessage(embed);
                return;
            }

            // Send the final Embed with the outfit image as a link, the submitter, and the id of the image.
            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .setAuthor(embedSubmitter)
                    .setImage(embedLink)
                    .setFooter(id);
            channel.sendMessage(embed);
        }
        catch (Exception e) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(Color.RED)
                    .addField("ERROR:", "Something went wrong...")
                    .addField("Output:", e.toString())
                    .setFooter("Contact Crablet#9999 and show him this error.");
            channel.sendMessage(embed);
        }
    }
}
