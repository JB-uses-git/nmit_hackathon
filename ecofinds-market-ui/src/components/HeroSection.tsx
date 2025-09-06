import { Button } from "@/components/ui/button";
import heroImage from "@/assets/hero-image.jpg";

const HeroSection = () => {
  return (
    <section className="relative py-20 lg:py-32 overflow-hidden">
      {/* Background with overlay */}
      <div className="absolute inset-0">
        <img 
          src={heroImage} 
          alt="Sustainable marketplace community"
          className="w-full h-full object-cover"
        />
        <div className="absolute inset-0 bg-gradient-to-r from-primary/90 to-primary/70" />
      </div>
      
      {/* Content */}
      <div className="relative container">
        <div className="max-w-2xl">
          <h1 className="text-4xl lg:text-6xl font-bold text-primary-foreground mb-6">
            Give Pre-loved Items a
            <span className="block text-accent"> New Beginning</span>
          </h1>
          <p className="text-lg lg:text-xl text-primary-foreground/90 mb-8 leading-relaxed">
            Discover unique second-hand treasures while reducing your environmental impact. 
            Join our community of conscious consumers making sustainable choices.
          </p>
          <div className="flex flex-col sm:flex-row gap-4">
            <Button 
              size="lg" 
              className="bg-accent text-accent-foreground hover:bg-accent/90 shadow-eco px-8"
            >
              Start Browsing
            </Button>
            <Button 
              variant="outline" 
              size="lg"
              className="border-primary-foreground/30 text-primary-foreground hover:bg-primary-foreground/10 px-8"
            >
              Sell Your Items
            </Button>
          </div>
        </div>
      </div>
    </section>
  );
};

export default HeroSection;