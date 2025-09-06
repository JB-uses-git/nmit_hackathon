class Product {
  final int id;
  final String title;
  final String price;
  final String originalPrice;
  final String imageUrl;
  final String condition;
  final String ecoScore;
  final String seller;
  bool isLiked;

  Product({
    required this.id,
    required this.title,
    required this.price,
    required this.originalPrice,
    required this.imageUrl,
    required this.condition,
    required this.ecoScore,
    required this.seller,
    this.isLiked = false,
  });

  void toggleLike() {
    isLiked = !isLiked;
  }

  double get savings {
    final original = double.tryParse(originalPrice.replaceAll('\$', '')) ?? 0;
    final current = double.tryParse(price.replaceAll('\$', '')) ?? 0;
    return ((original - current) / original * 100);
  }
}
