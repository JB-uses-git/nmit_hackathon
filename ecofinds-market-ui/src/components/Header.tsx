import { Search, ShoppingCart, User, Leaf } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

const Header = () => {
  return (
    <header className="sticky top-0 z-50 w-full border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
      <div className="container flex h-16 items-center justify-between">
        {/* Logo */}
        <div className="flex items-center gap-2">
          <div className="flex h-8 w-8 items-center justify-center rounded-full bg-gradient-eco">
            <Leaf className="h-5 w-5 text-primary-foreground" />
          </div>
          <span className="text-xl font-bold text-foreground">EcoFinds</span>
        </div>

        {/* Search Bar */}
        <div className="relative flex-1 max-w-md mx-8">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
          <Input
            placeholder="Search for sustainable treasures..."
            className="pl-10 bg-secondary/50 border-border"
          />
        </div>

        {/* Navigation */}
        <nav className="flex items-center gap-4">
          <Button variant="ghost" size="sm" className="text-foreground hover:bg-secondary">
            Categories
          </Button>
          <Button variant="ghost" size="sm" className="text-foreground hover:bg-secondary">
            Sell
          </Button>
          <Button variant="ghost" size="icon" className="text-foreground hover:bg-secondary">
            <ShoppingCart className="h-5 w-5" />
          </Button>
          <Button variant="ghost" size="icon" className="text-foreground hover:bg-secondary">
            <User className="h-5 w-5" />
          </Button>
        </nav>
      </div>
    </header>
  );
};

export default Header;