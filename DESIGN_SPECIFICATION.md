# Game Recommender - Design System Specification

## Overview
This design specification is inspired by Docker Desktop's clean, modern interface and adapted for a game recommendation web application. The design emphasizes usability, visual hierarchy, and a professional appearance suitable for gaming content.

## Color Palette

### Primary Colors
```css
:root {
  /* Primary Brand Colors */
  --primary-blue: #4A90E2;
  --primary-blue-dark: #357ABD;
  --primary-gradient: linear-gradient(135deg, #4A90E2, #6B73FF);
  
  /* Secondary Colors */
  --secondary-purple: #6B73FF;
  --secondary-purple-light: #8B93FF;
  
  /* Neutral Colors */
  --sidebar-dark: #2C3E50;
  --sidebar-dark-hover: #34495E;
  --background-primary: #FFFFFF;
  --background-secondary: #F8F9FA;
  --background-tertiary: #E9ECEF;
  
  /* Accent Colors */
  --accent-orange: #FF6B35;
  --accent-green: #27AE60;
  --accent-red: #E74C3C;
  --accent-yellow: #F39C12;
  
  /* Text Colors */
  --text-primary: #2C3E50;
  --text-secondary: #7F8C8D;
  --text-muted: #BDC3C7;
  --text-white: #FFFFFF;
  
  /* Border Colors */
  --border-light: #E1E8ED;
  --border-medium: #D1D9E0;
  --border-dark: #BDC3C7;
}
```

### Color Usage Guidelines
- **Primary Blue**: Main navigation, primary buttons, links
- **Secondary Purple**: Hover states, secondary actions
- **Accent Orange**: Notifications, warnings, featured content
- **Accent Green**: Success states, positive ratings
- **Accent Red**: Error states, negative ratings
- **Sidebar Dark**: Navigation sidebar, headers

## Typography

### Font Stack
```css
font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 
             'Oxygen', 'Ubuntu', 'Cantarell', sans-serif;
```

### Type Scale
```css
:root {
  /* Font Sizes */
  --font-xs: 0.75rem;    /* 12px */
  --font-sm: 0.875rem;   /* 14px */
  --font-base: 1rem;     /* 16px */
  --font-lg: 1.125rem;   /* 18px */
  --font-xl: 1.25rem;    /* 20px */
  --font-2xl: 1.5rem;    /* 24px */
  --font-3xl: 1.875rem;  /* 30px */
  --font-4xl: 2.25rem;   /* 36px */
  
  /* Font Weights */
  --font-light: 300;
  --font-normal: 400;
  --font-medium: 500;
  --font-semibold: 600;
  --font-bold: 700;
  
  /* Line Heights */
  --leading-tight: 1.25;
  --leading-normal: 1.5;
  --leading-relaxed: 1.75;
}
```

### Typography Hierarchy
- **H1**: `--font-4xl`, `--font-bold` - Page titles
- **H2**: `--font-3xl`, `--font-semibold` - Section headers
- **H3**: `--font-2xl`, `--font-semibold` - Subsection headers
- **H4**: `--font-xl`, `--font-medium` - Card titles
- **Body**: `--font-base`, `--font-normal` - General content
- **Small**: `--font-sm`, `--font-normal` - Secondary text
- **Caption**: `--font-xs`, `--font-normal` - Metadata, labels

## Spacing System

### Base Unit: 4px
```css
:root {
  --space-1: 0.25rem;   /* 4px */
  --space-2: 0.5rem;    /* 8px */
  --space-3: 0.75rem;   /* 12px */
  --space-4: 1rem;      /* 16px */
  --space-5: 1.25rem;   /* 20px */
  --space-6: 1.5rem;    /* 24px */
  --space-8: 2rem;      /* 32px */
  --space-10: 2.5rem;   /* 40px */
  --space-12: 3rem;     /* 48px */
  --space-16: 4rem;     /* 64px */
  --space-20: 5rem;     /* 80px */
}
```

## Layout System

### Grid Structure
- **Sidebar Width**: 240px (fixed)
- **Main Content**: Flexible, min-width 800px
- **Container Max Width**: 1200px
- **Breakpoints**:
  - Mobile: 768px and below
  - Tablet: 769px - 1024px
  - Desktop: 1025px and above

### Layout Components

#### Sidebar Navigation
```css
.sidebar {
  width: 240px;
  background: var(--sidebar-dark);
  height: 100vh;
  position: fixed;
  left: 0;
  top: 0;
}

.sidebar-item {
  padding: var(--space-3) var(--space-4);
  color: var(--text-white);
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.sidebar-item:hover {
  background: var(--sidebar-dark-hover);
}
```

#### Main Content Area
```css
.main-content {
  margin-left: 240px;
  padding: var(--space-6);
  background: var(--background-secondary);
  min-height: 100vh;
}
```

## Component Specifications

### Game Cards
```css
.game-card {
  background: var(--background-primary);
  border-radius: 8px;
  padding: var(--space-4);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border: 1px solid var(--border-light);
  transition: all 0.2s ease;
}

.game-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}
```

### Buttons
```css
.btn-primary {
  background: var(--primary-gradient);
  color: var(--text-white);
  padding: var(--space-3) var(--space-6);
  border-radius: 6px;
  font-weight: var(--font-medium);
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-secondary {
  background: transparent;
  color: var(--primary-blue);
  border: 1px solid var(--primary-blue);
  padding: var(--space-3) var(--space-6);
  border-radius: 6px;
  font-weight: var(--font-medium);
  cursor: pointer;
  transition: all 0.2s ease;
}
```

### Form Elements
```css
.form-input {
  padding: var(--space-3) var(--space-4);
  border: 1px solid var(--border-medium);
  border-radius: 6px;
  font-size: var(--font-base);
  background: var(--background-primary);
  transition: border-color 0.2s ease;
}

.form-input:focus {
  outline: none;
  border-color: var(--primary-blue);
  box-shadow: 0 0 0 3px rgba(74, 144, 226, 0.1);
}
```

## UI Patterns

### Navigation Structure
```
├── Dashboard
├── Browse Games
│   ├── By Genre
│   ├── By Rating
│   └── New Releases
├── My Recommendations
├── Favorites
└── Settings
```

### Page Layouts

#### Dashboard Layout
- Header with search bar and user actions
- Quick stats cards (Total games, Recommendations, etc.)
- Featured recommendations grid
- Recent activity feed

#### Game Browse Layout
- Filter sidebar (genres, ratings, platforms)
- Sort controls (rating, release date, popularity)
- Game grid with pagination
- Game detail modal/page

#### Game Detail Layout
- Hero image/video
- Game information panel
- Screenshots gallery
- Reviews and ratings
- Similar games recommendations

## Interactive States

### Hover States
- **Cards**: Subtle lift with increased shadow
- **Buttons**: Slight color darkening
- **Navigation**: Background color change

### Active States
- **Navigation**: Left border accent + background change
- **Buttons**: Pressed state with slight scale
- **Form inputs**: Focus ring with brand color

### Loading States
- **Cards**: Skeleton loading animation
- **Lists**: Progressive loading with shimmer effect
- **Buttons**: Spinner with disabled state

## Accessibility Guidelines

### Color Contrast
- Text on background: Minimum 4.5:1 ratio
- Interactive elements: Minimum 3:1 ratio
- Focus indicators: High contrast, visible outline

### Interactive Elements
- Minimum touch target: 44px × 44px
- Keyboard navigation support
- Screen reader friendly labels
- Focus management for modals

## Implementation Notes

### CSS Architecture
- Use CSS custom properties for theming
- Implement utility classes for common patterns
- Component-based CSS organization
- Mobile-first responsive design

### Framework Recommendations
- **CSS Framework**: Tailwind CSS or custom utility system
- **Component Library**: React/Vue components
- **Icons**: Feather Icons or Heroicons
- **Animations**: Framer Motion or CSS transitions

### Performance Considerations
- Optimize images (WebP format, lazy loading)
- Use CSS Grid/Flexbox for layouts
- Minimize CSS bundle size
- Implement critical CSS loading

## Future Enhancements

### Dark Mode Support
```css
[data-theme="dark"] {
  --background-primary: #1A1A1A;
  --background-secondary: #2D2D2D;
  --text-primary: #FFFFFF;
  --text-secondary: #B0B0B0;
  --sidebar-dark: #000000;
}
```

### Animation System
- Page transitions
- Micro-interactions
- Loading animations
- Hover effects

### Responsive Breakpoints
- Mobile navigation drawer
- Collapsible sidebar
- Responsive grid systems
- Touch-friendly interactions

---

## Current Implementation Progress ✅

### Backend Infrastructure (COMPLETED)
- [x] **Spring Boot Foundation**
  - ✅ Web application setup with Thymeleaf
  - ✅ Basic game card display functionality
  - ✅ Controller structure (Home, Game, Recommendation)
  - ✅ Development environment configuration

- [x] **AWS S3 Cloud Storage Integration**
  - ✅ S3 client configuration (`S3Config`)
  - ✅ Image upload service (`S3Service`)
  - ✅ Automatic thumbnail generation
  - ✅ Multiple image format support
  - ✅ Comprehensive test coverage

- [x] **Image Processing Infrastructure**
  - ✅ Thumbnailator library integration
  - ✅ Aspect ratio preservation
  - ✅ Multiple image size generation (original + thumbnail)
  - ✅ Error handling and validation

- [x] **Game Data Management**
  - ✅ Recommendation engine core logic
  - ✅ Game data models and DTOs
  - ✅ Test data utilities for development

### Technical Architecture Decisions
- **Cloud Storage**: AWS S3 for scalable image storage
- **Image Processing**: Thumbnailator for high-quality image resizing
- **Testing**: Comprehensive unit tests for all services
- **Error Handling**: Robust null checks and validation
- **Code Organization**: Clean separation of concerns

---

## Implementation Checklist

### Frontend Implementation (NEXT PHASE)
- [ ] Set up CSS custom properties
- [ ] Create base typography styles
- [ ] Implement layout grid system
- [ ] Build component library
- [ ] Add interactive states
- [ ] Test accessibility compliance
- [ ] Optimize for performance
- [ ] Add responsive breakpoints
- [ ] Implement dark mode toggle
- [ ] Create style guide documentation
