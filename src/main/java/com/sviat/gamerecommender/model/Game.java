package com.sviat.gamerecommender.model;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = Game.Builder.class)
public class Game {
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private String thumbnailUrl;
    private String developer;
    private String publisher;
    private Set<String> genres;
    private Set<String> tags;
    private int metacriticScore;
    private AgeRating ageRating;
    private Double userScore;
    private int reviewCount;
    private Set<String> platforms;
    private BigDecimal price;
    private boolean isMultiplayer;
    private int playtimeHours;

    private Game(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.imageUrl = builder.imageUrl;
        this.thumbnailUrl = builder.thumbnailUrl;
        this.developer = builder.developer;
        this.publisher = builder.publisher;
        this.genres = builder.genres;
        this.tags = builder.tags;
        this.metacriticScore = builder.metacriticScore;
        this.ageRating = builder.ageRating;
        this.userScore = builder.userScore;
        this.reviewCount = builder.reviewCount;
        this.platforms = builder.platforms;
        this.price = builder.price;
        this.isMultiplayer = builder.isMultiplayer;
        this.playtimeHours = builder.playtimeHours;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public String getDeveloper() {
        return this.developer;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public Set<String> getGenres() {
        return this.genres;
    }

    public Set<String> getTags() {
        return this.tags;
    }

    public int getMetacriticScore() {
        return this.metacriticScore;
    }

    public AgeRating getAgeRating() {
        return this.ageRating;
    }

    public Double getUserScore() {
        return this.userScore;
    }

    public int getReviewCount() {
        return this.reviewCount;
    }

    public Set<String> getPlatforms() {
        return this.platforms;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public boolean isMultiplayer() {
        return this.isMultiplayer;
    }

    public int getPlaytimeHours() {
        return this.playtimeHours;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private String id;
        private String title;
        private String description;
        private String imageUrl;
        private String thumbnailUrl;
        private String developer;
        private String publisher;
        private Set<String> genres;
        private Set<String> tags;
        private int metacriticScore;
        private AgeRating ageRating;
        private Double userScore;
        private int reviewCount;
        private Set<String> platforms;
        private BigDecimal price;
        private boolean isMultiplayer;
        private int playtimeHours;

        private Builder() {}

        @JsonProperty("id")
        public Builder id(String id) {
            this.id = id;
            return this;
        }

        @JsonProperty("title")
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        @JsonProperty("description")
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        @JsonProperty("imageUrl")
        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        @JsonProperty("thumbnailUrl")
        public Builder thumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
            return this;
        }

        @JsonProperty("developer")
        public Builder developer(String developer) {
            this.developer = developer;
            return this;
        }

        @JsonProperty("publisher")
        public Builder publisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        @JsonProperty("genres")
        public Builder genres(Set<String> genres) {
            this.genres = genres;
            return this;
        }

        @JsonProperty("tags")
        public Builder tags(Set<String> tags) {
            this.tags = tags;
            return this;
        }

        @JsonProperty("metacriticScore")
        public Builder metacriticScore(int metacriticScore) {
            this.metacriticScore = metacriticScore;
            return this;
        }

        @JsonProperty("ageRating")
        public Builder ageRating(AgeRating ageRating) {
            this.ageRating = ageRating;
            return this;
        }

        @JsonProperty("userScore")
        public Builder userScore(Double userScore) {
            this.userScore = userScore;
            return this;
        }

        @JsonProperty("reviewCount")
        public Builder reviewCount(int reviewCount) {
            this.reviewCount = reviewCount;
            return this;
        }

        @JsonProperty("platforms")
        public Builder platforms(Set<String> platforms) {
            this.platforms = platforms;
            return this;
        }

        @JsonProperty("price")
        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        @JsonProperty("isMultiplayer")
        public Builder isMultiplayer(boolean isMultiplayer) {
            this.isMultiplayer = isMultiplayer;
            return this;
        }

        @JsonProperty("playtimeHours")
        public Builder playtimeHours(int playtimeHours) {
            this.playtimeHours = playtimeHours;
            return this;
        }

        public Game build() {
            return new Game(this);
        }
    }

    public void updateField(String fieldName, Object value) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(this, value);
        } catch (Exception e) {
            throw new RuntimeException("Error updating field: " + fieldName, e);
        }
    }
}