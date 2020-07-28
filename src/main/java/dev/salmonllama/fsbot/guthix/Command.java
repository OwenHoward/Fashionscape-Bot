/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.guthix;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public abstract class Command {
    public abstract String name();
    public abstract String description();
    public abstract String usage();
    public abstract String category();
    public abstract CommandPermission permission();
    public abstract Collection<String> aliases();

    public abstract void onCommand(CommandContext ctx);

    public void invoke(final CommandContext ctx) {
        CompletableFuture.runAsync(() -> onCommand(ctx));
    }
}
