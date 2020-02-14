/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.utilities.database;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.ast.Table;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import org.javacord.api.DiscordApi;
import dev.salmonllama.fsbot.utilities.Outfit;
import org.javacord.api.entity.server.Server;
import dev.salmonllama.fsbot.utilities.exceptions.OutfitNotFoundException;

import java.util.*;

public class DatabaseUtilities {

    private final RethinkDB r;
    private final Connection conn;
    private final DiscordApi api;

    public DatabaseUtilities(RethinkDB r, Connection conn, DiscordApi api) {
        this.r = r;
        this.conn = conn;
        this.api = api;
    }

    private Table getTable(String table) {
        if (this.r.db("fsbot").tableList().contains(table).run(this.conn)) {
            return this.r.db("fsbot").table(table);
        }
        else {
            return null;
        }
    }

    private boolean validateId(String id) {
        return r.db("fsbot").table("outfits").getField("id").contains(id).run(this.conn);
    }

    public boolean tagExists(String tag) {
        return r.db("fsbot").table("outfits").getField("tag").distinct().contains(tag).run(this.conn);
    }

    public int countTags() {
        return this.r.db("fsbot").table("outfits").getField("tag").distinct().count().run(this.conn);
    }

    public ArrayList<String> getTags() {
        return r.db("fsbot").table("outfits").getField("tag").distinct().run(this.conn);
    }

    public Outfit getOutfitFromId(String id) throws OutfitNotFoundException {
        if (!this.validateId(id)) throw new OutfitNotFoundException();

        HashMap sample = this.r.db("fsbot").table("outfits")
                .get(id)
                .run(this.conn);

        String tag = sample.get("tag").toString();
        String link = sample.get("link").toString();
        String submitterId = sample.get("submitter").toString();

        String submitterName = api.getUserById(submitterId).join().getDiscriminatedName();

        return new Outfit(id, tag, submitterName, link);
    }

    public String removeFromDatabase(String id) throws OutfitNotFoundException {
        if (!this.validateId(id)) throw new OutfitNotFoundException();

        // Remove outfit return deletion status. 0 = failed, >= 1 = success
        HashMap deletion = r.db("fsbot").table("outfits").get(id).delete().run(conn);

        return deletion.get("deleted").toString();
    }

    public Outfit randomOutfit() {
        HashMap sample = this.r.db("fsbot").table("outfits")
                .sample(1).nth(0)
                .run(this.conn);

        String id = sample.get("id").toString();
        String tag = sample.get("tag").toString();
        String link = sample.get("link").toString();
        String submitterId = sample.get("submitter").toString();

        String submitterName = api.getUserById(submitterId).join().getDiscriminatedName();

        return new Outfit(id, tag, submitterName, link);
    }

    public Outfit randomTaggedOutfit(String targetTag) {
        HashMap sample = r.db("fsbot").table("outfits")
                .filter(row -> row.getField("tag").eq(targetTag))
                .sample(1).nth(0)
                .run(conn);

        String id = String.valueOf(sample.get("id"));
        String tag = String.valueOf(sample.get("tag"));
        String link = String.valueOf(sample.get("link"));
        String submitterId = String.valueOf(sample.get("submitter"));

        String submitterName = api.getUserById(submitterId).join().getDiscriminatedName(); // Try a thenAccept with a thenApply

        return new Outfit(id, tag, submitterName, link);
    }

    public String changeOutfitTag(String outfitId, String newTag) throws OutfitNotFoundException {
        if (!this.validateId(outfitId)) throw new OutfitNotFoundException();

        HashMap replacement = this.r.db("fsbot").table("outfits").get(outfitId).update(r.hashMap("tag", newTag)).run(this.conn);

        return replacement.get("replaced").toString();
    }

    public Outfit getOutfitBySubmitter(String userId) {
        HashMap sample = r.db("fsbot").table("outfits")
                .filter(row -> row.getField("submitter").eq(userId))
                .sample(1).nth(0)
                .run(conn);

        String id = String.valueOf(sample.get("id"));
        String tag = String.valueOf(sample.get("tag"));
        String link = String.valueOf(sample.get("link"));
        String submitterId = String.valueOf(sample.get("submitter"));

        String submitterName = api.getUserById(submitterId).join().getDiscriminatedName();

        return new Outfit(id, tag, submitterName, link);
    }

    public long getSubmitterCount(String submitter) {
        return r.db("fsbot").table("outfits")
                .filter(
                        row -> row.getField("submitter").eq(submitter)
                )
                .count()
                .run(conn);
    }

    public List<String> getSubmitterIds(String submitter) {
        List<String> ids = new ArrayList<>();
        Cursor<String> cursor = r.db("fsbot").table("outfits")
                .filter(
                        row -> row.getField("submitter").eq(submitter)
                )
                .getField("id")
                .run(conn);

        cursor.forEach(ids::add);

        return ids;
    }

    public void updateSubmitter(String submitter, String newSubmitter) {
        // Add feature to return update-error?
        r.db("fsbot").table("outfits")
                .filter(r.hashMap("submitter", submitter))
                .update(r.hashMap("submitter", newSubmitter))
                .run(conn);
    }

    public void newServerProcess(Server server) {

        if (this.r.db("fsbot").table("serverConf").contains(server.getIdAsString()).run(this.conn)) {
            return;
        }

        String serverName = server.getName();
        String serverId = server.getIdAsString();
        String logChannel = "null";
        String giveawayChannel = "null";
        String welcomeChannel = "null";
        String defaultWelcome = "welcome to the server";

        this.r.db("fsbot").table("serverConf").insert(
                this.r.hashMap("id", serverId)
                .with("name", serverName)
                .with("logChannel", logChannel)
                .with("giveawayChannel", giveawayChannel)
                .with("welcomeMsg", defaultWelcome)
                .with("welcomeChannel", welcomeChannel)
                .with("prefix", "~")
        ).run(this.conn);
    }

    public void tableSetup() { // TODO: Fix this -- invert conditionals, just create the tables. -> if *not* exist then create
        // Check for database existence, if not, create
        if (r.dbList().contains("fsbot").run(conn)) {
//            System.out.println("database 'fsbot' already exists.");
        }
        else {
            r.dbCreate("fsbot").run(conn);
            System.out.println("database fsbot did not exist, and has been created");
        }

        // Check for channels table existence, if not, create
        if (r.db("fsbot").tableList().contains("channels").run(conn)) {
//            System.out.println("table channels already exists");
        }
        else {
            r.db("fsbot").tableCreate("channels").run(conn);
            System.out.println("table channels did not exist, and has been created.");
        }

        // Check for serverconf table existence, if not, create
        if (r.db("fsbot").tableList().contains("serverConf").run(conn)) {
//            System.out.println("table serverConf already exists");
        }
        else {
            r.db("fsbot").tableCreate("serverConf").run(conn);
            System.out.println("table serverConf did not exist, and has been created");
        }

        // Check for permissions table existene, if not, create
        if (r.db("fsbot").tableList().contains("permissions").run(conn)) {
//            System.out.println("table permissions already exists");
        }
        else {
            r.db("fsbot").tableCreate("permissions").run(conn);
            System.out.println("table permissions did not exist and has been created");
        }

        // Check for outfits table existence, if not, create
        if (r.db("fsbot").tableList().contains("outfits").run(conn)) {
//            System.out.println("table outfits already exists");
        }
        else {
            r.db("fsbot").tableCreate("outfits").run(conn);
            System.out.println("table outfits did not exist and has been created");
        }

        // Check for colourRoles table existence, if not, create
        if (r.db("fsbot").tableList().contains("colourRoles").run(conn)) {
//            System.out.println("table colourRoles already exists");
        }
        else {
            r.db("fsbot").tableCreate("colourRoles").run(conn);
            System.out.println("table colourRoles did not exist and has been created");
        }
    }
}
