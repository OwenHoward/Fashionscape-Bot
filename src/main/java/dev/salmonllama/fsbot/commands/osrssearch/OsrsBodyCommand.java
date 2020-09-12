/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.osrssearch;

import dev.salmonllama.fsbot.endpoints.scapefashion.ScapeFashionConnection;
import dev.salmonllama.fsbot.endpoints.scapefashion.ScapeFashionSlotOsrs;
import dev.salmonllama.fsbot.guthix.*;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class OsrsBodyCommand extends Command {
    @Override public String name() { return "OSRS Body"; }
    @Override public String description() { return "Searches scape.fashion for body slot items."; }
    @Override public String usage() { return "osrsbody <#color or item name"; }
    @Override public CommandCategory category() { return CommandCategory.OSRS_ITEM_SEARCH; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.NONE); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Collections.singletonList("07body")); }

    @Override
    public void onCommand(CommandContext ctx) {
        if (ctx.getArgs().length == 0) {
            ctx.reply("Specify something");
            return;
        }

        var args = ctx.getArgs();
        if (OsrsSearchUtilities.isColor(args[0])) {
            // Color search
            ScapeFashionConnection conn = new ScapeFashionConnection();
            try { // TODO: Commands with items instead of colors do not work
                var bestMatch = conn.osrsColor(Strings.join(Arrays.asList(args), ' '), ScapeFashionSlotOsrs.BODY);
                OsrsSearchUtilities.sendResult(bestMatch, ctx.getChannel());
            } catch (Exception e) {
                OsrsSearchUtilities.handleException(e, ctx);
            }
        } else {
            // Item search
        }
    }
}
