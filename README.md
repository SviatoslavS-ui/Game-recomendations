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
- **Cyberpunk Design System**
  - Modern cyberpunk-themed UI with cyan/coral color scheme
  - Centralized CSS variables architecture for maintainability
  - Consistent typography using Orbitron and Rajdhani fonts
  - Advanced glow effects and smooth transitions
  - Custom SVG icons with cyberpunk styling and hover effects
  - SVG noise filters for authentic cyberpunk texture effects
  
- **Component Architecture**
  - **Cyberpunk Header Fragment**
    - Fixed header with dynamic page titles
    - Action buttons (Quick Search, About) with gradient styling
    - Real-time stats counter integration
    - Responsive design with mobile optimization
  - **Sidebar Navigation Fragment**
    - Reusable navigation component
    - Active state management
    - Consistent styling across all pages
  
- **Enhanced UI Components**
  - Grid-based layout for game cards
  - Interactive game cards with clickable areas
  - **Enhanced Game Details Modal**
    - Comprehensive game information display
    - Tags integration for better categorization
    - Async data loading with promise-based architecture
    - Loading states and error handling
    - Responsive design with smooth transitions
    - Robust null/incomplete data handling
    - Related games display with rating-based sorting
    - API endpoint constants for maintainability
    - Caching mechanism for repeated requests

### Core Services
- **Game Management**
  - Comprehensive game data models
  - Multi-platform support
  - Metacritic score integration
  - **Enhanced GetRecommendationByRelease** - Remastered using releaseDate for chronological sorting
  - Advanced filtering and sorting algorithms

- **Recommendation Engine**
  - **Multi-filter recommendation system** - Combines genre and tag filters with weighted scoring
  - **Intelligent scoring algorithm** - Calculates match quality based on both quantity and precision
  - **Genre-weighted scoring** - Genres weighted higher than tags (1.3:1.0 ratio)
  - **Perfect match detection** - 100% matches score higher than partial matches
  - **Precision-based ranking** - Considers the ratio of matches to filter criteria
  - **Metacritic tiebreaker** - Uses metacritic scores to resolve equal match scores
  - **Null-safe filtering** - Gracefully handles null or empty filter criteria

- **Image Processing**
  - Automatic thumbnail generation
  - Multiple image format support
  - Aspect ratio preservation

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
│       │   │   ├── variables.css     # Centralized design system
│       │   │   ├── sidebar.css       # Sidebar component styles
│       │   │   ├── header.css        # Header component styles
│       │   │   ├── main.css          # Layout and general styles
│       │   │   ├── svg-filters.css   # SVG noise filter utilities
│       │   │   └── game-card.css     # Game card components
│       │   ├── js/
│       │   │   ├── utils.js           # Utility functions and debug logger
│       │   │   ├── game-details-modal.js # Modal implementation
│       │   │   └── game-card.js       # Game card interactions
│       │   └── images/
│       ├── templates/                # Thymeleaf templates
       │   ├── fragments/            # Reusable components
       │   │   ├── header.html       # Cyberpunk header fragment
       │   │   ├── sidebar.html      # Navigation sidebar fragment
       │   │   ├── svg-filters.html  # SVG noise filter definitions
       │   │   └── game-card.html    # Game card fragment
       │   └── pages/                # Main page templates
       │       ├── dashboard.html    # Dashboard page
       │       └── games.html        # All games page
       └── application.properties    # App configuration
│
└── test/                            # Test files
    ├── java/
    │   └── com/sviat/gamerecommender/service/
    │       ├── S3ServiceTest.java
    │       └── TestGameData.java
    └── javascript/                  # Frontend tests
        ├── integration-modal.test.js # Parameterized integration tests
        ├── modal-basic.test.js      # Basic modal functionality tests
        └── babel.config.js          # Babel configuration for Jest

## Technical Details

- Built with Java 21
- Uses JSON for data storage
- Implements comprehensive unit testing
- Leverages Stream API for efficient data processing
- Uses builder pattern for object creation

### SVG Noise Filter System
- Reusable SVG filter definitions in fragments/svg-filters.html
- Four filter variations: basic noise, blur noise, cyberpunk grain, and subtle grain
- Utility classes in svg-filters.css for easy application
- Applied to game sections for authentic cyberpunk texture
- Configurable opacity and color variations
- Z-index control classes for layering effects

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
The project includes comprehensive test coverage with:

### Frontend Testing (100% Pass Rate)
- **Modal Integration Tests** - Complete modal lifecycle testing
  - Parameterized test approach for related games
  - Edge case testing (empty/single/multiple related games)
  - API endpoint constants integration
  - Null/incomplete data handling
- **Basic Modal Tests** - Core functionality verification
- **Advanced Testing Patterns**
  - Parameterized testing for reusable test cases
  - Test case generation from data objects
  - Comprehensive edge case coverage

### Backend Testing
- **Recommendation Engine Test Suite (43 Tests)**
  - **Requirement-based tests (R1-R8)** - Validates core recommendation requirements
  - **Parameterized test cases** - Data-driven tests for multiple scenarios
  - **Edge case handling** - Tests for non-existent genres, zero results
  - **Algorithm verification** - Direct testing of scoring algorithm
  - **Weight influence tests** - Verifies genre/tag weighting system
  - **Multi-filter tests** - Tests combinations of genre and tag filters
  - **Release date tests** - Verifies chronological sorting
  - **Metacritic score tests** - Validates score-based ordering
  - **Developer-specific tests** - Tests filtering by game developer

- **Test Infrastructure**
  - **TestGameData** - Standard game objects for consistent testing
  - **RecommendationTestData** - Specialized test games and parameterized test cases
  - **BaseServiceTest** - Common setup for service-level tests
  - **Reflection-based testing** - For private method verification
  - **Clean test isolation** - Prevents test interference

### Test Infrastructure
- Jest test runner with JSDOM environment
- Mock data and HTML fixtures
- Parameterized testing pattern for reusable test cases
- Test case generation from data objects
- Async/await testing patterns
- Promise-based function testing
- Error handling and edge case coverage
- API endpoint constants integration

### Running Tests
```bash
# Run all tests
npm test

# Run specific test suite
npx jest src/test/javascript/integration-modal.test.js

# Run tests with verbose output
npm test -- --verbose

# Run tests with coverage report
npm test -- --coverage
```