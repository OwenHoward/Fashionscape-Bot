/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.commands.developer;

import dev.salmonllama.fsbot.guthix.Command;
import dev.salmonllama.fsbot.guthix.CommandContext;
import dev.salmonllama.fsbot.guthix.CommandPermission;
import dev.salmonllama.fsbot.guthix.PermissionType;
import dev.salmonllama.fsbot.utilities.database.DatabaseUtilities;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class EvalCommand extends Command {
    @Override public String name() { return "Eval"; }
    @Override public String description() { return "Evaluates the given parameters"; }
    @Override public String usage() { return "eval <statement>"; }
    @Override public String category() { return "Developer"; }
    @Override public CommandPermission permission() { return new CommandPermission(PermissionType.OWNER); }
    @Override public Collection<String> aliases() { return new ArrayList<>(Arrays.asList("eval", "ev")); }

    private final DatabaseUtilities db;

    public EvalCommand(DatabaseUtilities db) {
        this.db = db;
    }

    @Override
    public void onCommand(CommandContext ctx) {
        String statement = String.join(" ", ctx.getArgs());

        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");

        engine.put("db", db);

        try {
            Object result = engine.eval(statement);
            ctx.reply(result.toString());
        }
        catch (Exception e) {
            ctx.reply(e.getMessage());
        }
    }
}
