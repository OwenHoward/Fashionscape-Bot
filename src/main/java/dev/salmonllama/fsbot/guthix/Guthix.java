/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.guthix;

import dev.salmonllama.fsbot.commands.general.*;
import dev.salmonllama.fsbot.commands.staff.OutfitInfoCommand;
import dev.salmonllama.fsbot.commands.staff.*;
import dev.salmonllama.fsbot.commands.developer.InviteCommand;
import dev.salmonllama.fsbot.commands.developer.CreateGalleryCommand;
import dev.salmonllama.fsbot.commands.developer.EvalCommand;
import dev.salmonllama.fsbot.utilities.database.DatabaseUtilities;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import dev.salmonllama.fsbot.commands.developer.TestCommand;

import java.util.Collection;
import java.util.HashMap;

/*
 * Guthix is Fashionscape Bot's command repository and dispatcher
 */
public class Guthix implements MessageCreateListener {
    @SuppressWarnings("unused")
    private DiscordApi api;
    private DatabaseUtilities db;

    private Registry registry;
    private PermissionManager manager;

    public Guthix(DiscordApi api, DatabaseUtilities db) {
        this.api = api;
        api.addMessageCreateListener(this);

        this.db = db;

        manager = new PermissionManager();
        registry = new Registry();

        initCommands();
    }

    public void initCommands() {
        // Developer Commands
        addCommand(new TestCommand());
        addCommand(new EvalCommand(db));
        addCommand(new CreateGalleryCommand());
        addCommand(new InviteCommand());

        // Staff Commands
        addCommand(new EchoCommand());
        addCommand(new GetServersCommand());
        addCommand(new AddColorCommand());
        addCommand(new GetOutfitCommand());
        addCommand(new RetagCommand(db));
        addCommand(new RemoveOutfitCommand(db));
        addCommand(new OutfitInfoCommand());
        addCommand(new SetStatusCommand());
        addCommand(new WelcomeMessageCommand());
        addCommand(new ShowGalleriesCommand());

        // General Commands
        addCommand(new PingCommand());
        addCommand(new ColorCommand());
        addCommand(new ColorsCommand());
        addCommand(new OutfitCommand());
        addCommand(new HelpCommand(this));
        addCommand(new StatsCommand());
    }

    public void addCommand(Command cmd) {
        registry.addCommand(cmd);
//        return cmd;
    }

    public Collection<Command> listCommands() {
        return registry.listCommands();
    }

    public HashMap<String, Command> mapCommands() {
        return registry.mapCommands();
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        MessageAuthor author = event.getMessageAuthor();

        if (manager.sourceIsValid(author)) {

        } else {
            return;
        }

        String content = event.getMessageContent();
        String contentLower = content.toLowerCase();

        if (registry.startsWithPrefix(contentLower)) {

        } else {
            return;
        }

        RegistryCommand rComm = registry.getCommandInfo(content);
        String cmdString = rComm.getCommand().toLowerCase();

        if (registry.isCommandAlias(cmdString)) {

        } else {
            return;
        }

        String[] cmdArgs = rComm.getArgs();

        Command cmd = registry.findCommand(cmdString).orElse(null); // TODO: default command here

        CommandContext ctx = new CommandContext.CommandContextBuilder(
                event,
                cmd,
                cmdString,
                cmdArgs
        ).build();

        if (manager.hasPermission(cmd.permission(), ctx)) {

        } else {
            return;
        }

        cmd.invoke(ctx);
    }
}
