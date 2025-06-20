package com.sviat.gamerecommender.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class User {
    private String id;
    private String username;
    private String email;
    private Set<String> favoriteGenres;
    private Set<String> preferredPlatforms;
    private BigDecimal maxPrice;
    private Set<String> ownedGames;
    private Map<Game, Integer> playedGames;
    private double averagePlaytime;
    private int totalGamesPlayed;
    private LocalDate lastActiveDate;
    private Set<String> wishlist;

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public Set<String> getPreferredPlatforms() {
        return this.preferredPlatforms;
    }

    public BigDecimal getMaxPrice() {
        return this.maxPrice;
    }

    public Set<String> getOwnedGames() {
        return this.ownedGames;
    }

    public Map<Game, Integer> getPlayedGames() {
        return this.playedGames;
    }

    public double getAveragePlaytime() {
        return this.averagePlaytime;
    }

    public int getTotalGamesPlayed() {
        return this.totalGamesPlayed;
    }

    public LocalDate getLastActiveDate() {
        return this.lastActiveDate;
    }

    public Set<String> getWishlist() {
        return this.wishlist;
    }

    public Set<String> getFavoriteGenres() {
        return this.favoriteGenres;
    }

    public static class Builder {
        private String id;
        private String username;
        private String email;
        private Set<String> favoriteGenres;
        private Set<String> preferredPlatforms;
        private BigDecimal maxPrice;
        private Set<String> ownedGames;
        private Map<Game, Integer> playedGames;
        private double averagePlaytime;
        private int totalGamesPlayed;
        private LocalDate lastActiveDate;
        private Set<String> wishlist;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder favoriteGenres(Set<String> favoriteGenres) {
            this.favoriteGenres = favoriteGenres;
            return this;
        }

        public Builder preferredPlatforms(Set<String> preferredPlatforms) {
            this.preferredPlatforms = preferredPlatforms;
            return this;
        }

        public Builder maxPrice(BigDecimal maxPrice) {
            this.maxPrice = maxPrice;
            return this;
        }

        public Builder ownedGames(Set<String> ownedGames) {
            this.ownedGames = ownedGames;
            return this;
        }

        public Builder playedGames(Map<Game, Integer> playedGames) {
            this.playedGames = playedGames;
            return this;
        }

        public Builder averagePlaytime(double averagePlaytime) {
            this.averagePlaytime = averagePlaytime;
            return this;
        }

        public Builder totalGamesPlayed(int totalGamesPlayed) {
            this.totalGamesPlayed = totalGamesPlayed;
            return this;
        }

        public Builder lastActiveDate(LocalDate lastActiveDate) {
            this.lastActiveDate = lastActiveDate;
            return this;
        }

        public Builder wishlist(Set<String> wishlist) {
            this.wishlist = wishlist;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    private User(User.Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.email = builder.email;
        this.favoriteGenres = builder.favoriteGenres;
        this.preferredPlatforms = builder.preferredPlatforms;
        this.maxPrice = builder.maxPrice;
        this.ownedGames = builder.ownedGames;
        this.playedGames = builder.playedGames;
        this.averagePlaytime = builder.averagePlaytime;
        this.totalGamesPlayed = builder.totalGamesPlayed;
        this.lastActiveDate = builder.lastActiveDate;
        this.wishlist = builder.wishlist;
    }
}
