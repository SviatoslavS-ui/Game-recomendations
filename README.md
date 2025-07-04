# Game Recommendation System

A Java project that demonstrates the use of modern Java features to build a game recommendation system. The project extensively uses Stream API, lambda expressions, and modern Java collections to process and filter game data.

## Project Overview

This system provides game recommendations based on various criteria such as genres, platforms, developers, and metacritic scores. It includes a robust data processing pipeline from JSON files to Java objects and various filtering mechanisms.

## Implemented Features

1. **Data Models**:
   - Game model with comprehensive attributes (title, genres, platforms, scores, etc.)
   - Support for multiple game genres and platforms
   - Metacritic scoring system integration

2. **Core Services**:
   - `JsonService`: Handles JSON file processing and data serialization
   - `GameDatabase`: Manages game data storage and basic CRUD operations
   - `RecommendationEngine`: Provides sophisticated game filtering and recommendations
   - `StatisticsService`: Calculates various gaming statistics and metrics

3. **Recommendation Features**:
   - Genre-based recommendations with multi-genre support
   - Platform-specific game filtering
   - Developer-focused game lists
   - Metacritic score-based sorting and filtering

## Project Structure

    src/
    ├── main/
    │   ├── java/
    │   │   └── com/sviat/gamerecommender/
    │   │       ├── model/              # Data models
    │   │       ├── repository/         # Data access layer
    │   │       ├── service/            # Business logic
    │   │       │   ├── GameDatabase    # Game data management
    │   │       │   ├── JsonService     # JSON processing
    │   │       │   ├── RecommendationEngine  # Recommendation logic
    │   │       │   └── StatisticsService     # Statistics calculations
    │   │       ├── util/              # Utility classes
    │   │       └── Main.java          # Application entry point
    │   └── resources/
    │       └── data/
    │           └── games.json         # Game data
    └── test/
        └── java/
            └── com/sviat/gamerecommender/service/
                ├── BaseServiceTest
                ├── GameDatabaseTest
                ├── JsonServiceTest
                ├── RecommendationEngineTest
                ├── StatisticsServiceTest
                └── TestGameData

## Technical Details

- Built with Java 24
- Uses JSON for data storage
- Implements comprehensive unit testing
- Leverages Stream API for efficient data processing
- Uses builder pattern for object creation

## Getting Started

1. Clone the repository
2. Ensure Java 24 is installed
3. Run `mvn compile` to build the project
4. Run `mvn test` to execute tests
5. Use `mvn exec:java -Dexec.mainClass="com.sviat.gamerecommender.Main"` to run the application

## Testing

The project includes extensive test coverage with:
- Unit tests for all core services
- Integration tests for the recommendation engine
- Test data sets for verification