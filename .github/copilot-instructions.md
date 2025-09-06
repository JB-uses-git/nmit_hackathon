# EcoFinds - Sustainable Second-Hand Marketplace

This Android application implements a complete second-hand marketplace platform with the following key features:

## Implemented Features
- User authentication (register/login) with session management
- Product listing creation and management (CRUD operations)
- Product browsing with category filtering and keyword search
- Shopping cart functionality
- Purchase system and history tracking
- User profile management
- Material Design UI with responsive layouts

## Architecture
- **Database**: Room database with proper entity relationships
- **UI**: Material Design components with RecyclerView adapters
- **Navigation**: Activity-based navigation with bottom navigation bar
- **Data Persistence**: Local SQLite database via Room
- **Image Handling**: Glide library with placeholder support

## Technical Stack
- Kotlin programming language
- Room database for data persistence
- Material Components for modern UI
- Coroutines for asynchronous operations
- Glide for image loading

## Key Activities
- MainActivity: Product browsing with search and filters
- LoginActivity/RegisterActivity: User authentication
- AddProductActivity: Product creation and editing
- ProductDetailActivity: Detailed product view with purchase options
- CartActivity: Shopping cart management
- PurchaseHistoryActivity: Purchase tracking
- ProfileActivity: User profile and product management

The application follows Android best practices with proper data layer separation, responsive UI design, and efficient resource management. Ready for further enhancement with features like real image upload, payment integration, and advanced social features.

## Project Status
✅ All core requirements implemented and functional
✅ Complete CRUD operations for all entities
✅ Full user workflow from registration to purchase
✅ Responsive Material Design interface
✅ Proper error handling and validation

Ready for testing and deployment!
