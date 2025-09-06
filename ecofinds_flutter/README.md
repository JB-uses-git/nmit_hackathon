# EcoFinds Flutter App

A sustainable marketplace mobile application built with Flutter, converted from the React web version.

## Features

- 🌱 **Eco-friendly Focus**: Promoting sustainable consumption through second-hand goods
- 📱 **Mobile-First Design**: Optimized for Android and iOS devices
- 🎨 **Modern UI**: Clean, intuitive interface with Material Design
- 💚 **Product Discovery**: Browse curated pre-loved items
- ❤️ **Favorites System**: Save items you love
- 🔍 **Search Functionality**: Find specific items easily
- 📊 **Eco Scoring**: Environmental impact ratings for products

## Project Structure

```
lib/
├── main.dart                 # App entry point
├── models/
│   └── product.dart         # Product data model
├── providers/
│   └── product_provider.dart # State management
├── screens/
│   └── home_screen.dart     # Main home screen
├── theme/
│   └── app_theme.dart       # App theming and colors
└── widgets/
    ├── custom_app_bar.dart  # Custom app bar component
    ├── hero_section.dart    # Hero banner component
    ├── product_card.dart    # Product card component
    └── product_grid.dart    # Product grid layout
```

## Getting Started

### Prerequisites

- Flutter SDK (latest stable version)
- Android Studio / VS Code
- Android device or emulator

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/JB-uses-git/nmit_hackathon.git
   cd nmit_hackathon/ecofinds_flutter
   ```

2. **Install dependencies**
   ```bash
   flutter pub get
   ```

3. **Run the app**
   ```bash
   flutter run
   ```

## Dependencies

- `flutter_staggered_grid_view`: For masonry grid layout
- `cached_network_image`: Efficient image loading and caching
- `provider`: State management solution
- `flutter_svg`: SVG image support

## Key Components Converted from React

### React → Flutter Conversions

| React Component | Flutter Equivalent |
|---|---|
| `Header.tsx` | `CustomAppBar` (SliverAppBar) |
| `HeroSection.tsx` | `HeroSection` (Container with decoration) |
| `ProductGrid.tsx` | `ProductGrid` (MasonryGridView) |
| `ProductCard.tsx` | `ProductCard` (Card widget) |
| CSS Styling | `AppTheme` class with ThemeData |
| React Context | Provider pattern |

### Design System

- **Colors**: Green-focused palette promoting eco-friendliness
- **Typography**: Poppins font family for modern readability
- **Components**: Material Design 3 components
- **Layout**: Responsive design with proper spacing

## Features in Detail

### 1. Product Cards
- High-quality product images
- Price comparison (current vs original)
- Eco-score badges
- Condition indicators
- Favorite/like functionality

### 2. Search & Discovery
- Real-time search
- Category filtering
- Sustainable product recommendations

### 3. User Experience
- Smooth scrolling
- Optimized image loading
- Responsive grid layout
- Touch-friendly interactions

## Building for Production

### Android
```bash
flutter build apk --release
```

### iOS (requires macOS)
```bash
flutter build ios --release
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Roadmap

- [ ] User authentication
- [ ] Shopping cart functionality
- [ ] Payment integration
- [ ] Push notifications
- [ ] Advanced filtering
- [ ] Seller dashboard
- [ ] Chat system
- [ ] Offline support

## License

This project is part of the ODOO x NMIT Hackathon.

---

**Happy Sustainable Shopping! 🌱📱**
