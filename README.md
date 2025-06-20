# Game Recommendation System

A Java project to practice lambda expressions and Stream API by building a game recommendation system.

## Project Structure

```
src/
├── main/
│   └── java/
│       └── com/yourname/gamerecommender/
│           ├── Main.java                    # Entry point
│           ├── model/
│           │   ├── Game.java               # Game data model
│           │   └── User.java               # User data model
│           ├── service/
│           │   ├── GameDatabase.java       # Game data management
│           │   ├── RecommendationEngine.java # Core recommendation logic
│           │   └── StatisticsService.java  # Statistics calculations
│           └── util/
│               └── SearchUtil.java         # Search utilities
└── test/
    └── java/
        └── com/yourname/gamerecommender/
            └── service/
                ├── RecommendationEngineTest.java
                └── StatisticsServiceTest.java
```

## Features to Implement

1. **Game Model**: Title, genre, rating, release year, platform, etc.
2. **User Model**: Name, played games, preferred genres, ratings given
3. **Recommendation Engine**: Filter and sort games using Stream API
4. **Statistics**: Calculate averages, popular genres using Collectors
5. **Search**: Find games by various criteria using lambda expressions

## Maven Commands

- `mvn compile` - Compile the project
- `mvn test` - Run tests
- `mvn exec:java -Dexec.mainClass="com.yourname.gamerecommender.Main"` - Run the application

## Getting Started

1. Replace `com.yourname` with your actual package name
2. Implement the model classes first
3. Create sample data in GameDatabase
4. Build the recommendation logic using Stream API and lambda expressions
5. Add tests to verify functionality
