package com.sviat.gamerecommender.model;

public enum Genre {
    ACTION("Action"),
    ADVENTURE("Adventure"),
    RPG("Role-Playing"),
    MMORPG("Massively-Multiplayer Role-Playing"),
    STRATEGY("Strategy"),
    TURN_BASED_STRATEGY("Turn-Based Strategy"),
    REAL_TIME_STRATEGY("Real-Time Strategy"),
    SIMULATION("Simulation"),
    SPORTS("Sports"),
    RACING("Racing"),
    HORROR("Horror"),
    SHOOTER("Shooter"),
    PLATFORMER("Platformer"),
    FIGHTING("Fighting"),
    INDIE("Indie"),
    CRIME("Crime"),
    WESTERN("Western"),
    SURVIVAL("Survival"),
    SOULSLIKE("Soulslike"),
    TACTICAL("Tactical"),
    SLASHER("Slasher");

    private final String displayName;

    Genre(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
