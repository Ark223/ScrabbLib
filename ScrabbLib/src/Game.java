
import java.io.PrintStream;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game
{
    private final int size = 15;
    private final String language;
    private ScrabbLib scrabbLib;
    private int turnForPlayer;
    private char[][] board;
    private ArrayList<Move> history;
    private ArrayList<Player> playerInfo;
    private List<Character> setOfTiles;
    
    public Game() throws Exception { this("EN", 2); }
    public Game(String lang, int players) throws Exception {
        this.language = lang;
        this.scrabbLib = new ScrabbLib(lang);
        this.scrabbLib.initDictionary();
        this.turnForPlayer = 0;
        this.board = createNewBoard();
        this.history = new ArrayList<>();
        this.playerInfo = new ArrayList<>();
        this.setOfTiles = generateTiles();
        for (int i = 0; i < players; i++)
            this.playerInfo.add(new Player(takeTiles("")));
    }
    public Game(Game game) throws Exception {
        this.language = game.language;
        this.scrabbLib = game.scrabbLib;
        this.scrabbLib.initDictionary();
        this.board = game.board;
        this.history = game.history;
        this.playerInfo = game.playerInfo;
        this.setOfTiles = game.setOfTiles;
    }
    
    private int getPlayersCount() { return this.playerInfo.size(); }
    
    private List<Character> generateTiles() {
        return Utils.extractor(this.language.equals("EN") ?
            "12E9A9I8O6N6R6T4L4S4U4D3G2B2C2M2P2F2H2V2W2Y1K1J1X1Q1Z" :
            "9A8I7E6O5N5Z4R4W4Y3C3D3K3L3M3P3T2B2G2H2J2Ł2U1Ą1Ę1F1O1S1Z1Ć1Ń1Ź");
    }
    
    private String takeTiles(String rack) {
        int remain = 7 - rack.length();
        for (int i = 0; i < remain; i++)
        {
            if (this.setOfTiles.isEmpty()) return rack;
            int index = (int)(Math.random() * this.setOfTiles.size());
            rack += this.setOfTiles.get(index);
            this.setOfTiles.remove(index);
        }
        return rack;
    }
    
    private char[][] createNewBoard() {
        char[][] newBoard = new char[this.size][this.size];
        for (int i = 0; i < this.size; i++)
            for (int j = 0; j < this.size; j++)
                newBoard[i][j] = '-';
        return newBoard;
    }
    
    private int calculateScore(Move move) {
        return 0;
    }
    
    private boolean isValid(String word) {
        return scrabbLib.isValid(word);
    }
    
    private String readWord(Position start, Position end)
    {
        String word = "";
        ArrayList<Position> positions = Move.getPositions(start, end);
        for (Position pos : positions) word += this.board[pos.x][pos.y];
        return word;
    }
    
    private Position searcher(Position start, char dir)
    {
        Position offset = dir == 'U' ? new Position(0, -1) : dir == 'D' ?
            new Position(0, 1) : dir == 'R' ? new Position(1, 0) : new Position(-1, 0);
        Position pos = start.clone();
        while (true)
        {
            Position test = pos.add(offset);
            if (this.board[test.x][test.y] == '-' || test.isOutside()) break;
            pos = test;
        }
        return pos;
    }
    
    // Public
    
    public void displayBoard() throws Exception {
        PrintStream out = new PrintStream(System.out, true, "UTF-8");
        out.println("  ABCDEFGHIJKLMNO");
        for (int i = 0; i < this.size; i++)
        {
            out.print((i + 1) % 10 + " ");
            for (int j = 0; j < this.size; j++)
                out.print(this.board[j][i]);
            out.println();
        }
    }
    
    public void displayPlayersInfo() throws Exception {
        PrintStream out = new PrintStream(System.out, true, "UTF-8");
        for (int i = 0; i < this.playerInfo.size(); i++)
        {
            out.println("=========\nPlayer " + (i + 1) + ":");
            out.println("Rack: " + this.playerInfo.get(i).getRack());
            out.println("Score: " + this.playerInfo.get(i).getScore());
        }
    }
    
    public char[][] getBoard() {
        return this.board;
    }
    
    public String getPlayersRack() {
        return this.playerInfo.get(this.turnForPlayer).getRack();
    }
    
    public boolean isEmpty() {
        return Arrays.stream(this.board).map(CharBuffer::wrap)
            .flatMapToInt(CharBuffer::chars).filter(c -> c != '-').count() == 0;
    }
    
    public boolean isCrossing(Move move) {
        ArrayList<Position> positions = move.getPositions();
        Move.direction dir = move.getDir();
        Position s = positions.get(0).clone(), e = positions.get(positions.size() - 1).clone();
        Position c1 = dir == Move.direction.Horizontal ? new Position(s.x - 1, s.y) : new Position(s.x, s.y - 1);
        Position c2 = dir == Move.direction.Horizontal ? new Position(e.x + 1, e.y) : new Position(e.x, e.y + 1);
        return (c1.isInside() && this.board[c1.x][c1.y] != '-') ||
            (c2.isInside() && this.board[c2.x][c2.y] != '-') ||
            positions.stream().anyMatch(pos -> this.board[pos.x][pos.y] != '-');
    }
    
    public boolean isValid(Move move) {
        return this.isValid(move, this.playerInfo.get(this.turnForPlayer).getRack());
    }
    
    public boolean isValid(Move move, String rack) {
        if (this.isEmpty()) return this.isValid(move.getWord()) && move.onCenter();
        if (!this.isCrossing(move)) return false;
        Move.direction dir = move.getDir();
        ArrayList<Position> raw = move.getPositions();
        Position start = this.searcher(raw.get(0), dir == Move.direction.Horizontal ? 'L' : 'U');
        Position end = this.searcher(raw.get(raw.size() - 1), dir == Move.direction.Horizontal ? 'R' : 'D');
        String word = Utils.insertWord(this.readWord(start, end), move.getWord());
        if (!this.isValid(word)) return false;
        ArrayList<Position> positions = Move.getPositions(start, end);
        for (int i = 0; i < positions.size(); i++) {
            Position pos = positions.get(i);
            char letter = word.charAt(i);
            if (this.board[pos.x][pos.y] == '-') {
                int index = rack.contains(Character.toString(letter)) ?
                    rack.indexOf(letter) : rack.indexOf(Character.toLowerCase(letter));
                if (index < 0) return false;
                rack = Utils.removeAt(rack, index);
            }
            if (Character.toUpperCase(this.board[pos.x][pos.y]) == word.charAt(i)) {
                Position testStart = this.searcher(pos, dir == Move.direction.Horizontal ? 'U' : 'L');
                Position testEnd = this.searcher(pos, dir == Move.direction.Horizontal ? 'D' : 'R');
                if (!this.isValid(this.readWord(testStart, testEnd))) return false;
            }
            else if (this.board[pos.x][pos.y] != '-')
                return false;
        }
        return true;
    }
    
    public boolean tryMakeMove(Move move) {
        return this.tryMakeMove(move, this.getPlayersRack(), "");
    }
    
    public boolean tryMakeMove(Move move, String rack, String add) {
        if (!this.isValid(move, rack)) return false;
        this.makeMove(move, rack, add); return true;
    }
    
    public void makeMove(Move move) {
        this.makeMove(move, this.getPlayersRack(), "");
    }
    
    public void makeMove(Move move, String rack, String add) {
        String word = move.getWord();
        List<Character> usedTiles = new ArrayList<>();
        ArrayList<Position> positions = move.getPositions();
        for (int i = 0; i < positions.size(); i++) {
            int x = positions.get(i).getX(), y = positions.get(i).getY();
            if (this.board[x][y] == '-') usedTiles.add(word.charAt(i));
            this.board[x][y] = word.charAt(i);
        }
        for (char letter : usedTiles) {
            int index = rack.contains(Character.toString(letter)) ?
                rack.indexOf(letter) : rack.indexOf(Character.toLowerCase(letter));
            if (index >= 0) rack = Utils.removeAt(rack, index);
        }
        rack = !add.equals("") ? rack + add : this.takeTiles(rack);
        this.playerInfo.set(this.turnForPlayer, new Player(rack, calculateScore(move)));
        this.history.add(move);
        this.turnForPlayer = (this.turnForPlayer + 1) % getPlayersCount();
    }
    
    public void passTurn() {
        this.turnForPlayer = (this.turnForPlayer + 1) % getPlayersCount();
    }
}

