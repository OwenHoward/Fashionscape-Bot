/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.endpoints.scapefashion;

import java.util.ArrayList;
import java.util.Collection;

public class ScapeFashionResult {
    private final String link;
    private Collection<ScapeFashionItem> items;

    public ScapeFashionResult(String link) {
        this.link = link;
        items = new ArrayList<>();
    }

    public ScapeFashionResult addItem(ScapeFashionItem item) {
        this.items.add(item);
        return this;
    }

    public String getLink() {
        return link;
    }

    public Collection<ScapeFashionItem> getItems() {
        return items;
    }
}
