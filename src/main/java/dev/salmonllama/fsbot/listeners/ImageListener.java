/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.listeners;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import com.vdurmont.emoji.EmojiParser;
import okhttp3.*;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.json.JSONObject;
import dev.salmonllama.fsbot.config.BotConfig;

import java.awt.*;
import java.util.List;

public class ImageListener implements MessageCreateListener {

    final RethinkDB r;
    final Connection conn;

    public ImageListener(RethinkDB r, Connection conn) {
        this.r = r;
        this.conn = conn;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        event.getMessage().getUserAuthor().ifPresent(author -> {
            if (author.isBot()) {
                return;
            }
        });

        Message message = event.getMessage();
        String channel = event.getChannel().getIdAsString();
        String submitter = event.getMessage().getAuthor().getIdAsString();

        if (message.getAttachments().stream().noneMatch(MessageAttachment::isImage)) {
            return;
        }

        // If the message contains an image, check if the channel is being listened to for image storage

        String dbTable = null;

        if(r.db("fsbot").table("channels").g("cId").contains(channel).run(conn)) {
            Cursor cursor = r.db("fsbot")
                    .table("channels")
                    .filter(row -> row.getField("cId").eq(channel))
                    .getField("tag")
                    .run(conn);

            List tags = cursor.toList();

            for (Object tag : tags) {
                dbTable = tag.toString();
            }
        }
        else {
            return;
        }

        List<MessageAttachment> attachments = message.getAttachments();

        for (MessageAttachment attachment : attachments) {
            String discordLink = attachment.getUrl().toString();
            String imgurLink = null;

            // Upload the image to imgur
            try {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");

                RequestBody body = RequestBody.create(
                        mediaType,
                        String.format("------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"image\"\r\n\r\n%s\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--", discordLink));

                Request request = new Request.Builder()
                        .url("https://api.imgur.com/3/image")
                        .method("POST", body)
                        .header("Authorization", BotConfig.IMGUR_ID)
                        .header("Authorization", BotConfig.IMGUR_BEARER)
                        .build();

                Response response = client.newCall(request).execute();

                if (response.body() == null) {
                    event.getChannel().sendMessage("Something went wrong!");
                    return;
                }

                String jsonData = response.body().string();
                imgurLink = new JSONObject(jsonData).getJSONObject("data").getString("link");

                if (imgurLink == null) {
                    event.getChannel().sendMessage("Something went wrong!");
                    return;
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            r.db("fsbot").table("outfits").insert(
                    r.hashMap("link", imgurLink)
                            .with("submitter", submitter)
                            .with("tag", dbTable)
            ).run(conn);

            final String finalLink = imgurLink;
            Cursor imgId = r.db("fsbot")
                    .table("outfits")
                    .filter(row -> row.getField("link").eq(finalLink))
                    .getField("id")
                    .run(conn);

            String embedId = null;
            List imgIds = imgId.toList();
            for (Object id : imgIds) {
                embedId = id.toString();
            }

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Added an entry of tag " + dbTable)
                    .setColor(Color.GREEN)
                    .setThumbnail(imgurLink)
                    .setAuthor("Placeholder")
                    .setFooter(embedId);

            event.getApi().getUserById(submitter).thenAccept(user -> embed.setAuthor(user.getDiscriminatedName()));

            event.getApi().getChannelById(BotConfig.OUTFIT_LOG).ifPresent(chnl -> {
                chnl.asTextChannel().ifPresent(txtchnl -> {
                    txtchnl.sendMessage(embed).join();
                });
            });

            message.addReaction(EmojiParser.parseToUnicode(":heartpulse:"));
        }
    }
}