/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.osrssearch;

import dev.salmonllama.fsbot.guthix.*;

import java.util.ArrayList;
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

    }
}
