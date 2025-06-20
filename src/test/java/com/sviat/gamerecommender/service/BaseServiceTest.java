package com.sviat.gamerecommender.service;

import org.junit.jupiter.api.BeforeEach;

/**
 * Base test class that provides common setup for service layer tests.
 * Contains shared initialization of JsonService, GameDatabase, and other common components.
 */
public abstract class BaseServiceTest {
    protected JsonService jsonService;
    protected GameDatabase gameDatabase;

    @BeforeEach
    void baseSetUp() {
        jsonService = new JsonService();
        gameDatabase = new GameDatabase(jsonService);
    }
}
