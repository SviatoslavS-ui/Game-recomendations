# Game Recommendation System

A Spring Boot web application that provides personalized game recommendations with a modern, Docker-inspired UI. The system features AWS S3 integration for image storage and processing, along with a responsive grid-based layout for optimal user experience.

## Project Overview

This system provides game recommendations based on various criteria such as genres, platforms, developers, and metacritic scores. It features a responsive web interface with image handling capabilities, powered by AWS S3 for cloud storage and Thumbnailator for image processing.

## Key Features

### Backend
- **AWS S3 Integration**
  - Secure cloud storage for game images
  - Automatic thumbnail generation
  - Efficient image processing pipeline

### Frontend
- **Responsive Design**
  - Docker-inspired UI components
  - Grid-based layout for game cards
  - Smooth animations and transitions
  - Interactive game cards with clickable areas
  - Responsive game details modal with loading states

### Core Services
- **Game Management**
  - Comprehensive game data models
  - Multi-platform support
  - Metacritic score integration
  - Advanced filtering and sorting

- **Image Processing**
  - Automatic thumbnail generation
  - Multiple image format support
  - Aspect ratio preservation
  - Cloud-based storage solution

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/sviat/gamerecommender/
│   │       ├── config/               # Configuration classes
│   │       │   ├── S3Config.java     # AWS S3 configuration
│   │       │   └── GameConfig.java   # Application config-n
│   │       │
│   │       ├── controller/           # Web controllers
│   │       │   └── HomeController.java
│   │       │
│   │       ├── model/                # Data models
│   │       │   ├── Game.java
│   │       │   └── Platform.java
│   │       │
│   │       └── service/              # Business logic
│   │           ├── S3Service.java    # AWS S3 operations
│   │           ├── GameDatabase.java
│   │           └── RecommendationEngine.java
│   │
│   └── resources/
│       ├── static/                   # Static assets
│       │   ├── css/
│       │   └── images/
│       ├── templates/                # Thymeleaf templates
│       └── application.properties    # App configuration
│
└── test/                            # Test files
    └── java/
        └── com/sviat/gamerecommender/service/
            ├── S3ServiceTest.java
            └── TestGameData.java
```

## Technical Details

- Built with Java 21
- Uses JSON for data storage
- Implements comprehensive unit testing
- Leverages Stream API for efficient data processing
- Uses builder pattern for object creation

## Getting Started

### Prerequisites
- Java 21 or higher
- Maven 3.6 or higher
- AWS Account with S3 access
- AWS credentials configured (~/.aws/credentials or environment variables)

### Required Environment Variables
```
AWS_ACCESS_KEY_ID=your_access_key
AWS_SECRET_ACCESS_KEY=your_secret_key
AWS_REGION=your_region
AWS_S3_BUCKET=your_bucket_name
```

### Running the Application
1. Clone the repository
2. Configure AWS credentials
3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
5. Open `http://localhost:8080` in your browser

## Testing
The project includes extensive test coverage with:
- Unit tests for all core services
- Integration tests for the recommendation engine
- Test data sets for verification