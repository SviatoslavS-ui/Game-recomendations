package com.sviat.gamerecommender.model;

/**
 * Enum representing all possible game tags in the system.
 * Each tag has a display name that can be used for UI presentation.
 */
public enum Tags {
    OPEN_WORLD("Open World"),
    STORY_RICH("Story-Rich"),
    PvE_AND_PVP("PvE & PvP"),
    MULTIPLAYER("Multiplayer"),
    SINGLEPLAYER("Single-player"),
    ATMOSPHERIC("Atmospheric"),
    DIFFICULT("Difficult"),
    INDIE("Indie"),
    SCI_FI("Sci-Fi"),
    FANTASY("Fantasy"),
    PUZZLE("Puzzle"),
    MATURE("Mature"),
    MYSTERY("Mystery"),
    SPACE("Space"),
    ISOMETRIC("Isometric"),
    EXPLORATION("Exploration"),
    SKATEBOARDING("Skateboarding"),
    ARCADE("Arcade"),
    FPS("FPS"),
    CLASSIC("Classic"),
    FAMILY("Family"),
    DYSTOPIAN("Dystopian"),
    CINEMATIC("Cinematic"),
    HORROR("Horror"),
    THIRD_PERSON("Third-person"),
    TURN_BASED("Turn-based"),
    CHARACTER_CUSTOMIZATION("Character Customization"),
    DUNGEONS_AND_DRAGONS("Dungeons & Dragons"),
    WEAPONS("Weapons"),
    SANDBOX("Sandbox"),
    PHYSICS("Physics"),
    REAL_TIME_COMBAT("Real-Time Combat"),
    SQUAD_BASED("Squad-Based"),
    DETECTIVE("Detective"),
    UNDERWATER("Underwater"),
    PSYCHOLOGICAL("Psychological"),
    MEDIEVAL("Medieval"),
    HISTORICAL("Historical"),
    ALIENS("Aliens"),
    STEALTH("Stealth"),
    NAZI("Nazi"),
    CYBERPUNK("Cyberpunk"),
    STEAMPUNK("Steampunk");

    private final String displayName;

    /**
     * Constructor for Tags enum.
     * 
     * @param displayName The human-readable display name for the tag
     */
    Tags(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the tag.
     * 
     * @return The human-readable display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Converts a string to the corresponding Tags enum value.
     * Case-insensitive matching is performed.
     * 
     * @param tagName The tag name to convert
     * @return The corresponding Tags enum value, or null if no match is found
     */
    public static Tags fromString(String tagName) {
        if (tagName == null) {
            return null;
        }
        
        // Try direct match first
        try {
            return Tags.valueOf(tagName.toUpperCase().replace('-', '_').replace(' ', '_'));
        } catch (IllegalArgumentException e) {
            // If direct match fails, try matching by display name
            for (Tags tag : Tags.values()) {
                if (tag.getDisplayName().equalsIgnoreCase(tagName)) {
                    return tag;
                }
            }
            return null;
        }
    }
}
