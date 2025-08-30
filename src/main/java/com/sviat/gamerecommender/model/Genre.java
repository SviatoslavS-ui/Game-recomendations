package com.sviat.gamerecommender.model;

public enum Genre {
    ACTION("Action"),
    ADVENTURE("Adventure"),
    RPG("Role-Playing"),
    STRATEGY("Strategy"),
    SIMULATION("Simulation"),
    SPORTS("Sports"),
    RACING("Racing"),
    PUZZLE("Puzzle"),
    HORROR("Horror"),
    SHOOTER("Shooter"),
    PLATFORMER("Platformer"),
    FIGHTING("Fighting"),
    INDIE("Indie"),
    CASUAL("Casual"),
    EDUCATIONAL("Educational"),
    MUSIC("Music"),
    CRIME("Crime"),
    WESTERN("Western"),
    SCI_FI("Sci-Fi"),
    SURVIVAL("Survival"),
    SOULSLIKE("Soulslike"),
    TACTICAL("Tactical"),
    REAL_TIME("Real-Time");

    private final String displayName;

    Genre(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
