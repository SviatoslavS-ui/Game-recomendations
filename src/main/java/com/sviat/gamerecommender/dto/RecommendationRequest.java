package com.sviat.gamerecommender.dto;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

public class RecommendationRequest {
    private List<String> genre = new ArrayList<>();
    private List<String> tag = new ArrayList<>();

    // Getters and setters
    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    // Convert to Set for easier filtering
    public Set<String> getGenresAsSet() {
        return new HashSet<>(genre);
    }

    public Set<String> getTagsAsSet() {
        return new HashSet<>(tag);
    }
}
