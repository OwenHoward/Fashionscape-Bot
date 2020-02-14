/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.utilities.database;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;

import java.util.List;

public class ServerConfUtility {

    private static final RethinkDB r = RethinkDB.r;
    private static final Connection conn = r.connection().hostname("localhost").port(28015).connect();

    private static String serverId;

    public ServerConfUtility(String sId) {
        serverId = sId;
    }

    // TODO: Turn server into method args, not class field.

    public String getWelcomeMsg() {

        String welcomeMsg = null;

        Cursor welcomes = r.db("fsbot").table("serverConf")
                .filter(row -> row.getField("id").eq(serverId))
                .getField("welcomeMsg")
                .run(conn);
        List welcomeMsgs = welcomes.toList();
        for (Object msg : welcomeMsgs) {
            welcomeMsg = msg.toString();
        }

        return welcomeMsg;
    }

    public void setWelcomeMsg(String msg) {

        r.db("fsbot").table("serverConf")
                .get(serverId)
                .update(r.hashMap("welcomeMsg", msg))
                .run(conn);
    }

    public String getWelcomeChannel() {

        String welcomeChannel = null;

        Cursor channels = r.db("fsbot").table("serverConf")
                .filter(row -> row.getField("id").eq(serverId))
                .getField("welcomeChannel")
                .run(conn);
        List welcomeChannels = channels.toList();
        for (Object chnl : welcomeChannels) {
            welcomeChannel = chnl.toString();
        }

        return welcomeChannel;
    }

    public void setWelcomeChannel(String id) {

        r.db("fsbot").table("serverConf")
                .get(serverId)
                .update(r.hashMap("welcomeChannel", id))
                .run(conn);
    }
}
