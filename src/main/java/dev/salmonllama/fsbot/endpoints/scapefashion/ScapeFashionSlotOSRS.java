/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.endpoints.scapefashion;

import java.util.Optional;

// Source: https://github.com/ncpierson/fashionscape/blob/master/api/data/tools/slot.js
public enum ScapeFashionSlotOSRS {
    AMMUNITION("ammunition"),
    BODY("body"),
    CAPE("cape"),
    FEET("feet"),
    HAND("hand"),
    HEAD("head"),
    LEG("leg"),
    NECK("neck"),
    RING("ring"),
    SHIELD("shield"),
    WEAPON("weapon");

    private final String value;

    ScapeFashionSlotOSRS(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Optional<ScapeFashionSlotOSRS> matches(String s) {
        try {
            var slot = ScapeFashionSlotOSRS.valueOf(s);
            return Optional.of(slot);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
