package dev.salmonllama.fsbot.commands.general;

import dev.salmonllama.fsbot.guthix.*;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.util.Collection;
import java.util.Collections;

public class PrivacyCommand extends Command {
    @Override public String name() { return "Privacy"; }
    @Override public String description() { return "Directs users to the bot's privacy policy"; }
    @Override public String usage() { return "privacy"; }
    @Override public CommandCategory category() { return CommandCategory.GENERAL; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.NONE); }
    @Override public Collection<String> aliases() { return Collections.singletonList("privacy"); }


    @Override
    public void onCommand(CommandContext ctx) {
        String privacyUrl = "https://github.com/Salmonllama/Fashionscape-Bot/blob/database-rewrite/privacy.md"; // TODO: Change this to master

        EmbedBuilder response = new EmbedBuilder()
                .setTitle("Click Here to open")
                .setUrl(privacyUrl);

        EmbedBuilder response2 = new EmbedBuilder()
                .setTitle("Link")
                .setDescription(privacyUrl)
                .setAuthor(ctx.getApi().getYourself());

        ctx.reply(response).thenAcceptAsync(msg -> ctx.reply(response2));
    }
}
