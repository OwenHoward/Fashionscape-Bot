/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.utilities;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import dev.salmonllama.fsbot.utilities.database.RoleColourUtility;

import java.util.List;
import java.awt.Color;

public class ColorRole {
    public String id;
    public String name;

    public ColorRole(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static EmbedBuilder rolesListEmbed() {
        List<ColorRole> roles = RoleColourUtility.getAllRoles();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Available ColorRoles")
                .setColor(Color.GREEN)
                .setFooter("Showing " + roles.size() + " roles");

        StringBuilder builder = new StringBuilder();

        roles.forEach(role -> {
            builder.append(role.name).append("\n");
        });

        embed.addField("Roles:", builder.toString());

        return embed;
    }
}
