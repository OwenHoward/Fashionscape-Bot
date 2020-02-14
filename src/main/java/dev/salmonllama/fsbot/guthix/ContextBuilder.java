/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.guthix;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.server.Server;

import java.util.Collection;

public class ContextBuilder {
    private DiscordApi api;
    private Message message;
    private MessageAuthor author;
    private TextChannel channel;
    private Server server;
    private Command usedCommand;
    private String usedAlias;
    private String[] args;

    public ContextBuilder() {

    }

    public ContextBuilder setUsedCommand(Command commandUsed) {
        this.usedCommand = commandUsed;
        return this;
    }

    public ContextBuilder setUsedAlias(String alias) {
        this.usedAlias = alias;
        return this;
    }

    public ContextBuilder setArgs(String[] args) {
        this.args = args;
        return this;
    }

    ContextBuilder setMessage(Message message) {
        this.message = message;
        return this;
    }

    ContextBuilder setAuthor(MessageAuthor author) {
        this.author = author;
        return this;
    }

    ContextBuilder setChannel(TextChannel channel) {
        this.channel = channel;
        return this;
    }

    ContextBuilder setServer(Server server) {
        this.server = server;
        return this;
    }

    ContextBuilder setApi(DiscordApi api) {
        this.api = api;
        return this;
    }

    CommandContext build() {
        return new CommandContext(api, message, author, channel, server, usedAlias, usedCommand, args);
    }
}
