
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
        Move.direction direction = this.getDir();
        Position offset = new Position(direction == Move.direction.Horizontal
            ? 1 : 0, direction == Move.direction.Vertical ? 1 : 0);
        int len = this.getWord().length(), i = 0;
        while (i < len) {
            i++; positions.add(start);
            start = start.add(offset);
        }
        return positions;
    }
    
    public static ArrayList<Position> getPositions(Position start, Position end) {
        ArrayList<Position> positions = new ArrayList<>();
        Position temp = end.sub(start), t = start.clone();
        Position offset = new Position(temp.x != 0 ? 1 : 0, temp.y != 0 ? 1 : 0);
        int len = temp.x == 0 ? temp.y : temp.x, i = 0;
        while (i <= len) {
            i++; positions.add(t);
            t = t.add(offset);
        }
        return positions;
    }
    
    public boolean onCenter() {
        ArrayList<Position> positions = this.getPositions();
        return positions.stream().anyMatch(p -> p.x == 7 && p.y == 7);
    }
}

