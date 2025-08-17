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

### Core Services
- **Game Management**
  - Comprehensive game data models
  - Multi-platform support
  - Metacritic score integration
  - **Enhanced GetRecommendationByRelease** - Remastered using releaseDate for chronological sorting
  - Advanced filtering and sorting algorithms

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
        ├── setup/
        │   └── setupTests.js        # Jest configuration
        ├── mocks/
        │   ├── gameMocks.js         # Test data
        │   └── modalHtmlMock.js     # DOM fixtures
        └── unit/modal/              # Modal test suites
            ├── modalIntegration.test.js
            ├── dataHandling.test.js
            ├── modalControl.test.js
            └── eventListeners.test.js

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

### Frontend Testing (46 Tests - 100% Pass Rate)
- **Modal Integration Tests** (15 tests) - Complete modal lifecycle testing
- **Data Handling Tests** (21 tests) - Function-level testing for all modal utilities  
- **Modal Control Tests** (5 tests) - DOM manipulation and state management
- **Event Listeners Tests** (5 tests) - User interaction and event handling

### Backend Testing
- Unit tests for all core services (S3Service, RecommendationEngine)
- Integration tests for the recommendation engine
- Test data sets for verification

### Test Infrastructure
- Jest test runner with JSDOM environment
- Mock data and HTML fixtures
- Async/await testing patterns
- Promise-based function testing
- Error handling and edge case coverage

### Running Tests
```bash
# Run all tests
npm test

# Run specific test suite
npx jest src/test/javascript/unit/modal/modalIntegration.test.js

# Run tests with verbose output
npm test -- --verbose
```