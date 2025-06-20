package com.sviat.gamerecommender.model;

public enum Platform {
    PC("PC"), PLAYSTATION_5("PlayStation 5"), XBOX_SERIES("Xbox Series"), NINTENDO_SWITCH("Nintendo Switch"),
    MOBILE("Mobile"), VR("VR"), MAC("Mac"), LINUX("Linux");

    private final String displayName;

    Platform(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
