package com.sviat.gamerecommender.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AgeRating {
    EVERYONE("Everyone"),
    EVERYONE_10_PLUS("Everyone 10+"),
    TEEN("Teen"),
    MATURE("Mature"),
    ADULTS_ONLY("Adults Only");

    private final String displayName;

    AgeRating(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static AgeRating fromString(String value) {
        for (AgeRating rating : AgeRating.values()) {
            if (rating.displayName.equalsIgnoreCase(value)) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Unknown AgeRating: " + value);
    }
}
