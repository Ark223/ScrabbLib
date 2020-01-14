
public class Position implements Cloneable
{
    final int x;
    final int y;
    
    public Position(String pos) {
        this.x = (int)pos.replaceAll("\\d+", "").charAt(0) - 65;
        this.y = Integer.parseInt(pos.replaceAll("\\D+", "")) - 1;
    }
    
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    
    public Position add(Position pos) {
        return new Position(this.x + pos.x, this.y + pos.y);
    }
    
    public Position sub(Position pos) {
        return new Position(this.x - pos.x, this.y - pos.y);
    }
    
    @Override
    public Position clone() {
        return new Position(this.x, this.y);
    }
    
    @Override
    public boolean equals(Object obj) {
        Position pos = (Position)obj;
        return this.x == pos.x && this.y == pos.y;
    }
    
    @Override
    public int hashCode() {
        return 17 * (this.x + 85) + this.y;
    }
    
    @Override
    public String toString() {
        return "Position: (" + this.x + ", " + this.y + ")";
    }
    
    public boolean isInside() {
        return !this.isOutside();
    }
    
    public boolean isOutside() {
        return this.x < 0 || this.x > 14 || this.y < 0 || this.y > 14;
    }
    
    public ScrabbLib.premiumSquare isPremium() {
        char c = ScrabbLib.bonuses.charAt(this.x * 15 + this.y);
        return c == 'd' ? ScrabbLib.premiumSquare.DoubleLetter :
                c == 'D' ? ScrabbLib.premiumSquare.DoubleWord :
                c == 't' ? ScrabbLib.premiumSquare.TripleLetter :
                c == 'T' ? ScrabbLib.premiumSquare.TripleWord :
                ScrabbLib.premiumSquare.None;
    }
}

