/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.endpoints.scapefashion;

public class ScapeFashionItem {
    private final String name;
    private final String slot;
    private final String link;
    private final String[] colors;
    private final float match;

    private ScapeFashionItem(Builder builder) {
        this.name = builder.name;
        this.slot = builder.slot;
        this.link = builder.link;
        this.colors = builder.colors;
        this.match = builder.match;
    }

    public static class Builder {
        private String name;
        private String slot;
        private String link;
        private String[] colors;
        private float match;

        public Builder() {

        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setSlot(String slot) {
            this.slot = slot;
            return this;
        }

        public Builder setLink(String link) {
            this.link = link;
            return this;
        }

        public Builder setColors(String[] colors) {
            this.colors = colors;
            return this;
        }

        public Builder setMatch(float match) {
            this.match = match;
            return this;
        }

        public ScapeFashionItem build() {
            return new ScapeFashionItem(this);
        }
    }
}
