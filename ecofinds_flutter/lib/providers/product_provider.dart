import 'package:flutter/material.dart';
import '../models/product.dart';

class ProductProvider with ChangeNotifier {
  final List<Product> _products = [
    Product(
      id: 1,
      title: "Vintage Leather Jacket - Classic Brown",
      price: "\$45",
      originalPrice: "\$120",
      imageUrl: "https://images.unsplash.com/photo-1551698618-1dfe5d97d256?w=400&h=400&fit=crop",
      condition: "Excellent",
      ecoScore: "A+",
      seller: "Sarah M.",
      isLiked: false,
    ),
    Product(
      id: 2,
      title: "Mid-Century Modern Coffee Table",
      price: "\$185",
      originalPrice: "\$450",
      imageUrl: "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=400&h=400&fit=crop",
      condition: "Good",
      ecoScore: "A",
      seller: "Mike R.",
      isLiked: true,
    ),
    Product(
      id: 3,
      title: "Classic Literature Collection",
      price: "\$28",
      originalPrice: "\$85",
      imageUrl: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&h=400&fit=crop",
      condition: "Very Good",
      ecoScore: "A+",
      seller: "Emma L.",
      isLiked: false,
    ),
    Product(
      id: 4,
      title: "Vintage Leather Messenger Bag",
      price: "\$65",
      originalPrice: "\$180",
      imageUrl: "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400&h=400&fit=crop",
      condition: "Good",
      ecoScore: "A",
      seller: "Alex K.",
      isLiked: false,
    ),
    Product(
      id: 5,
      title: "Retro Gaming Console",
      price: "\$95",
      originalPrice: "\$250",
      imageUrl: "https://images.unsplash.com/photo-1493711662062-fa541adb3fc8?w=400&h=400&fit=crop",
      condition: "Excellent",
      ecoScore: "A+",
      seller: "Gaming Pro",
      isLiked: true,
    ),
    Product(
      id: 6,
      title: "Artisan Ceramic Vase Set",
      price: "\$75",
      originalPrice: "\$200",
      imageUrl: "https://images.unsplash.com/photo-1578749556568-bc2c40e68b61?w=400&h=400&fit=crop",
      condition: "Like New",
      ecoScore: "A+",
      seller: "Art Lover",
      isLiked: false,
    ),
  ];

  List<Product> get products => _products;

  List<Product> get likedProducts => _products.where((p) => p.isLiked).toList();

  void toggleProductLike(int productId) {
    final productIndex = _products.indexWhere((p) => p.id == productId);
    if (productIndex != -1) {
      _products[productIndex].toggleLike();
      notifyListeners();
    }
  }

  List<Product> searchProducts(String query) {
    if (query.isEmpty) return _products;
    return _products
        .where((product) =>
            product.title.toLowerCase().contains(query.toLowerCase()) ||
            product.seller.toLowerCase().contains(query.toLowerCase()))
        .toList();
  }
}
