import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Heart } from "lucide-react";

interface ProductCardProps {
  id: number;
  title: string;
  price: string;
  originalPrice?: string;
  image: string;
  condition: string;
  ecoScore: string;
  seller: string;
  isLiked?: boolean;
}

const ProductCard = ({ 
  title, 
  price, 
  originalPrice, 
  image, 
  condition, 
  ecoScore, 
  seller,
  isLiked = false 
}: ProductCardProps) => {
  return (
    <Card className="group cursor-pointer transition-all duration-300 hover:shadow-card-eco hover:-translate-y-1 bg-gradient-card border-border/50">
      <CardContent className="p-0">
        {/* Image Container */}
        <div className="relative overflow-hidden rounded-t-lg">
          <img 
            src={image} 
            alt={title}
            className="w-full h-48 object-cover transition-transform duration-300 group-hover:scale-105"
          />
          
          {/* Eco Badge */}
          <div className="absolute top-3 left-3">
            <Badge className="bg-success text-success-foreground">
              {ecoScore} Eco Score
            </Badge>
          </div>
          
          {/* Like Button */}
          <Button
            variant="ghost"
            size="icon"
            className="absolute top-3 right-3 bg-background/80 hover:bg-background"
          >
            <Heart 
              className={`h-4 w-4 ${isLiked ? 'fill-destructive text-destructive' : 'text-muted-foreground'}`} 
            />
          </Button>
        </div>
        
        {/* Product Info */}
        <div className="p-4">
          <h3 className="font-semibold text-foreground mb-2 line-clamp-2">
            {title}
          </h3>
          
          <div className="flex items-center gap-2 mb-2">
            <span className="text-lg font-bold text-primary">{price}</span>
            {originalPrice && (
              <span className="text-sm text-muted-foreground line-through">
                {originalPrice}
              </span>
            )}
          </div>
          
          <div className="flex items-center justify-between text-sm text-muted-foreground mb-3">
            <Badge variant="secondary" className="text-xs">
              {condition}
            </Badge>
            <span>by {seller}</span>
          </div>
          
          <Button className="w-full bg-primary text-primary-foreground hover:bg-primary/90">
            View Details
          </Button>
        </div>
      </CardContent>
    </Card>
  );
};

export default ProductCard;