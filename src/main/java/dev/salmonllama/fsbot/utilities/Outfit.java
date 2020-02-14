/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.utilities;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;

public class Outfit {

    private String id;
    private String tag;
    private String submitter;
    private String link;
    private DiscordApi api;

    public Outfit(String id, String tag, String submitter, String link) {
        this.id = id;
        this.tag = tag;
        this.submitter = submitter;
        this.link = link;
    }

    public void setId(String id) { this.id = id; }
    public String getId() { return this.id; }

    public String getTag() { return this.tag; }

    // public void setTag(String tag) { this.tag = tag; }
    // public String getTag() { return this.tag; }

    // public void setSubmitter(String submitter) { this.submitter = submitter; }
    // public String getSubmitter() { return this.submitter; }

    // public void setLink(String link) { this.link = link; }
    // public String getLink() { return this.link; }

    public EmbedBuilder generateEmbed() {
        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(submitter + " | " + tag)
                .setImage(this.link)
                .setFooter(this.id);
    }

    public EmbedBuilder generateInfo() {
        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("Outfit Information")
                .setThumbnail(this.link)
                .addField("Submitter:", this.submitter, true)
                .addField("Tag:", this.tag, true)
                .addField("Id:", this.id)
                .addField("Link:", this.link);
    }

    public EmbedBuilder tagChangeEmbed(String changer, String oldTag, String newTag) {
        return new EmbedBuilder()
                .setColor(Color.YELLOW)
                .setTitle(String.format("Tag changed by %s", changer))
                .setThumbnail(this.link)
                .addField("Old Tag:", oldTag)
                .addField("New Tag:", newTag)
                .setFooter(this.id);
    }
}
