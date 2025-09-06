# EcoFinds - Sustainable Second-Hand Marketplace

## Project Overview
EcoFinds is a comprehensive Android application that serves as a sustainable second-hand marketplace. The app promotes eco-friendly consumption by enabling users to buy and sell pre-owned goods, extending product lifecycles and reducing waste.

## Features Implemented

### ✅ User Authentication
- **Secure Registration**: Email and password based registration with validation
- **Login System**: Secure authentication with session management
- **Profile Management**: Users can view and edit their profile information

### ✅ Product Management
- **Product Listing Creation**: Add products with title, description, category, price, and image placeholder
- **CRUD Operations**: Create, Read, Update, and Delete product listings
- **Category System**: Predefined categories for better organization
- **Image Placeholders**: Ready for future image upload integration

### ✅ Browsing & Search
- **Product Browsing**: Grid layout displaying all available products
- **Category Filtering**: Filter products by multiple categories using chips
- **Keyword Search**: Search products by title keywords
- **Product Details**: Detailed view of individual products

### ✅ Shopping Features
- **Shopping Cart**: Add/remove products to/from cart
- **Purchase System**: Buy individual products or checkout entire cart
- **Purchase History**: View previously purchased items

### ✅ User Dashboard
- **Profile View**: Display user information with edit capabilities
- **My Products**: View and manage user's listed products
- **Navigation**: Bottom navigation for easy access to all features

## Technical Architecture

### Database (Room)
- **Entities**: User, Product, CartItem, Purchase
- **DAOs**: Separate data access objects for each entity
- **Relationships**: Foreign key relationships between entities

### UI Components
- **Material Design**: Modern UI following Material Design guidelines
- **RecyclerView**: Efficient list displays with custom adapters
- **Navigation**: Bottom navigation and activity-based navigation
- **Responsive Layout**: Optimized for various screen sizes

### Key Technologies
- **Kotlin**: Primary development language
- **Room Database**: Local data persistence
- **Material Components**: UI framework
- **Glide**: Image loading library
- **Coroutines**: Asynchronous programming

## Project Structure
```
app/src/main/
├── java/com/ecofinds/
│   ├── MainActivity.kt
│   ├── LoginActivity.kt
│   ├── RegisterActivity.kt
│   ├── AddProductActivity.kt
│   ├── ProductDetailActivity.kt
│   ├── CartActivity.kt
│   ├── PurchaseHistoryActivity.kt
│   ├── ProfileActivity.kt
│   ├── adapter/
│   │   ├── ProductAdapter.kt
│   │   └── CartAdapter.kt
│   ├── database/
│   │   └── EcoFindsDatabase.kt
│   ├── dao/
│   │   ├── UserDao.kt
│   │   ├── ProductDao.kt
│   │   ├── CartDao.kt
│   │   └── PurchaseDao.kt
│   ├── model/
│   │   ├── User.kt
│   │   ├── Product.kt
│   │   ├── CartItem.kt
│   │   └── Purchase.kt
│   └── utils/
│       ├── SessionManager.kt
│       └── Constants.kt
├── res/
│   ├── layout/ (All activity and item layouts)
│   ├── values/ (Strings, colors, themes)
│   ├── drawable/ (Vector icons and drawables)
│   └── menu/ (Navigation menus)
└── AndroidManifest.xml
```

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK API 24 or higher
- Kotlin support

### Installation
1. Clone or open the project in Android Studio
2. Sync project with Gradle files
3. Run the application on an emulator or physical device

### First Run
1. Register a new account or use existing credentials
2. Browse available products or add your own listings
3. Use search and filter features to find specific items
4. Add items to cart and proceed with purchases

## Core Workflows

### Seller Workflow
1. Register/Login → Profile Setup
2. Add Product → Fill details → Save
3. Manage Products → Edit/Delete listings
4. View Profile → Check listed products

### Buyer Workflow
1. Register/Login → Browse Products
2. Search/Filter → View Product Details
3. Add to Cart OR Buy Now
4. Checkout → View Purchase History

## Future Enhancements
- Real image upload and storage
- Push notifications
- In-app messaging between buyers/sellers
- Payment integration
- Location-based listings
- Product ratings and reviews
- Advanced search filters
- Social sharing features

## Database Schema

### Users Table
- id (Primary Key)
- username, email, password
- createdAt timestamp

### Products Table
- id (Primary Key)
- title, description, category, price
- imageUrl, sellerId (Foreign Key)
- isAvailable, createdAt

### CartItems Table
- id (Primary Key)
- userId (Foreign Key), productId (Foreign Key)
- quantity, addedAt

### Purchases Table
- id (Primary Key)
- buyerId (Foreign Key), productId (Foreign Key)
- purchasePrice, purchaseDate

## Contributing
This project serves as a foundation for a sustainable marketplace. Contributors can enhance existing features or add new functionality following the established patterns and architecture.

---
*EcoFinds - Making sustainable choices easier for everyone* 🌱
