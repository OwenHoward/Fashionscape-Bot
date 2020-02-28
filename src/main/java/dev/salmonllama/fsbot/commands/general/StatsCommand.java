package dev.salmonllama.fsbot.commands.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import dev.salmonllama.fsbot.database.controllers.OutfitController;
import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;

public class StatsCommand extends Command {
    @Override public String name() { return "Stats"; }
    @Override public String description() { return "Shows various stats from Fashionscape Bot"; }
    @Override public String usage() { return "stats"; }
    @Override public String category() { return "General"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.NONE); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("stats")); }

    @Override
    public void onCommand(CommandContext ctx) {
        // Stats to display:
        // Number of users
        // Number of servers
        // Number of galleries
        // Number of non-deleted images
        // Number of total images
        // CPU and RAM?
        int userCount = ctx.getApi().getCachedUsers().size(); // Will these be accurate with sharding?
        int serverCount = ctx.getApi().getServers().size();

        EmbedBuilder embed = new EmbedBuilder(); // TODO: Standard embeds yeah?
        embed.setTitle("Stats");
        embed.addField("Users:", String.valueOf(userCount));
        embed.addField("Servers:", String.valueOf(serverCount));

        OutfitController.countActiveOutfits().thenAccept(count -> embed.addField("Active outfits:", String.valueOf(count))).join();
        OutfitController.countTotalOutfits().thenAccept(count -> embed.addField("Total outifts:", String.valueOf(count))).join();
        OutfitController.countFeaturedOutfits().thenAccept(count -> embed.addField("Featured outfits:", String.valueOf(count))).join();

        ctx.reply(embed);
    }
}