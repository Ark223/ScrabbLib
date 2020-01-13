
public class Player
{
    private String rack;
    private int score;
    
    public Player(String rack) {
        this.rack = rack;
        this.score = 0;
    }
    
    public Player(String rack, int addScore) {
        this.rack = rack;
        this.score += addScore;
    }
    
    public String getRack() { return this.rack; }
    public int getScore() { return this.score; }
}

