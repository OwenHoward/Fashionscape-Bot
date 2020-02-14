/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.utilities.database;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import dev.salmonllama.fsbot.utilities.ColorRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoleColourUtility {

    private static RethinkDB r = RethinkDB.r;
    private static Connection conn = r.connection().hostname("localhost").port(28015).connect();

    public static void addColourRole(String colourName, String roleId) {
        r.db("fsbot").table("colourRoles").insert(
                r.hashMap("id", roleId)
                        .with("name", colourName)).run(conn);
    }

    public static void deleteColourRole() {

    }

    public static String getColour(String colourName) {
        String roleId = null;

        Cursor cursor = r.db("fsbot").table("colourRoles")
                .filter(row -> row.getField("name").eq(colourName))
                .getField("id")
                .run(conn);
        List roleIds = cursor.toList();
        for (Object id : roleIds) {
            roleId = id.toString();
        }

        return roleId;
    }

    public static List<ColorRole> getAllRoles() {
        List<ColorRole> allRoles = new ArrayList<>();

        Cursor cursor = r.db("fsbot").table("colourRoles")
                .run(conn);

        while (cursor.hasNext()) {
            HashMap role = (HashMap) cursor.next();

            String id = String.valueOf(role.get("id"));
            String name = String.valueOf(role.get("name"));

            allRoles.add(new ColorRole(id, name));
        }

        return allRoles;
    }

    public static String getAllRoleInfo() {
        return r.db("fsbot").table("colourRoles").run(conn);
    }
}
