
import java.util.ArrayList;

public class Move
{
    public enum direction {Horizontal, Vertical};
    private final Position pos;
    private final direction dir;
    private final String word;
    
    public Move(Position pos, direction dir, String word) {
        this.pos = pos;
        this.dir = dir;
        this.word = word;
    }
    
    public Position getPos() { return this.pos; }
    public direction getDir() { return this.dir; }
    public String getWord() { return this.word; }
    
    public ArrayList<Position> getPositions() {
        ArrayList<Position> positions = new ArrayList<>();
        Position start = this.getPos();
        positions.add(start.clone());
        Move.direction direction = this.getDir();
        for (int i = 1; i < this.getWord().length(); i++)
            positions.add(new Position(start.x + (direction == Move.direction.Horizontal ?
                i : 0), start.y + (direction == Move.direction.Vertical ? i : 0)));
        return positions;
    }
    
    public static ArrayList<Position> getPositions(Position start, Position end) {
        ArrayList<Position> positions = new ArrayList<>();
        Position temp = end.sub(start);
        for (int i = 0; i <= (temp.x == 0 ? temp.y : temp.x); i++)
            positions.add(new Position(temp.x != 0 ? (start.x + i) :
                start.x, temp.y != 0 ? (start.y + i) : start.y));
        return positions;
    }
    
    public boolean onCenter() {
        ArrayList<Position> positions = this.getPositions();
        return positions.stream().anyMatch(p -> p.x == 7 && p.y == 7);
    }
}

