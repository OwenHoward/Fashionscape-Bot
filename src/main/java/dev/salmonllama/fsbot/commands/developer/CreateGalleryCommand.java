/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.developer;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import dev.salmonllama.fsbot.config.BotConfig;
import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class CreateGalleryCommand extends Command { // TODO: This command needs help.
    @Override public String name() { return "Create Gallery"; }
    @Override public String description() { return "Creates a channel gallery, tracking any posted images"; }
    @Override public String usage() { return "creategallery <String tag>"; }
    @Override public String category() { return "Developer"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.OWNER); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("creategallery", "addgallery", "addgall")); }

    static final RethinkDB R = RethinkDB.r;
    static final Connection CONN = R.connection().hostname("localhost").port(28015).connect();

    @Override
    public void onCommand(CommandContext ctx) {
        Server server = ctx.getServer();
        TextChannel channel = ctx.getChannel();
        String[] args = ctx.getArgs();
        String targetChannelId = channel.getIdAsString();
        String targetChannelName = channel.asServerChannel().get().getName(); // TODO: un-band-aid this.
        String targetServerName = server.getName();
        String targetServerId = server.getIdAsString();

        if (!server.getIdAsString().equals(BotConfig.HOME_SERVER)) {
            channel.sendMessage("Fashion galleries can only be created in the Fashionscape server");
            return;
        }

        if (args.length < 1) {
            channel.sendMessage("No Arguments provided");
            return;
        }
        else if (args.length > 1) {
            channel.sendMessage("Too many arguments provided.");
            return;
        }

        String tag = args[0];

        if (R.db("fsbot").table("channels").getField("cId").contains(targetChannelId).run(CONN)) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(Color.RED)
                    .addField("ERROR", "This channel is already gathering images");
            channel.sendMessage(embed);
        }
        else {
            R.db("fsbot").table("channels").insert(
                    R.hashMap("sName", targetServerName)
                            .with("sId", targetServerId)
                            .with("cName", targetChannelName)
                            .with("cId", targetChannelId)
                            .with("tag", tag)
            ).run(CONN);

            R.db("fsbot").tableCreate(tag).run(CONN);

            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .addField("Success", "Channel added to storage with the following values:")
                    .addField("Channel Name:", targetChannelName)
                    .addField("Channel Id:", targetChannelId)
                    .addField("Tag:", tag)
                    .addField("End:", String.format("Table \"%s\" created for images in this channel", tag));
            channel.sendMessage(embed);
        }
    }
}
