import ProductCard from "./ProductCard";
import productJacket from "@/assets/product-jacket.jpg";
import productFurniture from "@/assets/product-furniture.jpg";
import productBooks from "@/assets/product-books.jpg";

const ProductGrid = () => {
  const products = [
    {
      id: 1,
      title: "Vintage Leather Jacket - Classic Brown",
      price: "$45",
      originalPrice: "$120",
      image: productJacket,
      condition: "Excellent",
      ecoScore: "A+",
      seller: "Sarah M.",
      isLiked: false
    },
    {
      id: 2,
      title: "Mid-Century Modern Coffee Table",
      price: "$185",
      originalPrice: "$450",
      image: productFurniture,
      condition: "Good", 
      ecoScore: "A",
      seller: "Mike R.",
      isLiked: true
    },
    {
      id: 3,
      title: "Classic Literature Collection",
      price: "$28",
      originalPrice: "$85",
      image: productBooks,
      condition: "Very Good",
      ecoScore: "A+",
      seller: "Emma L.",
      isLiked: false
    },
    {
      id: 4,
      title: "Vintage Leather Messenger Bag",
      price: "$65",
      originalPrice: "$180",
      image: productJacket,
      condition: "Good",
      ecoScore: "A",
      seller: "Alex K.",
      isLiked: false
    },
    {
      id: 5,
      title: "Retro Wooden Bookshelf",
      price: "$95",
      originalPrice: "$280",
      image: productFurniture,
      condition: "Excellent",
      ecoScore: "A+",
      seller: "Lisa P.",
      isLiked: true
    },
    {
      id: 6,
      title: "Philosophy & Science Books Bundle",
      price: "$35",
      originalPrice: "$120",
      image: productBooks,
      condition: "Good",
      ecoScore: "A",
      seller: "David S.",
      isLiked: false
    }
  ];

  return (
    <section className="py-16 lg:py-24">
      <div className="container">
        <div className="text-center mb-12">
          <h2 className="text-3xl lg:text-4xl font-bold text-foreground mb-4">
            Featured Sustainable Finds
          </h2>
          <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
            Discover unique pre-loved items from our community of conscious sellers
          </p>
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 lg:gap-8">
          {products.map((product) => (
            <ProductCard key={product.id} {...product} />
          ))}
        </div>
        
        <div className="text-center mt-12">
          <button className="bg-primary text-primary-foreground hover:bg-primary/90 px-8 py-3 rounded-lg font-semibold transition-colors">
            View All Products
          </button>
        </div>
      </div>
    </section>
  );
};

export default ProductGrid;