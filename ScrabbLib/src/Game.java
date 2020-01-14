
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
    
    public Game() throws Exception {
        this("EN", 2);
    }
    
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
    
    private int getPlayersCount() {
        return this.playerInfo.size();
    }
    
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
        return this.scrabbLib.isValid(word);
    }
    
    private String readWord(Position start, Position end) {
        return this.readWord(this.board, start, end);
    }
    
    private String readWord(char[][] board, Position start, Position end)
    {
        String word = "";
        ArrayList<Position> positions = Move.getPositions(start, end);
        for (Position pos : positions) word += board[pos.x][pos.y];
        return word;
    }
    
    private Position searcher(Position start, char dir) {
        return this.searcher(this.board, start, dir);
    }
    
    private Position searcher(char[][] board, Position start, char dir)
    {
        Position offset = dir == 'U' ? new Position(0, -1) : dir == 'D' ?
            new Position(0, 1) : dir == 'R' ? new Position(1, 0) : new Position(-1, 0);
        Position pos = start.clone();
        while (true)
        {
            Position test = pos.add(offset);
            if (test.isOutside()) break;
            if (board[test.x][test.y] == '-') break;
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
        char[][] b = new char[this.size][this.size];
        for (int i = 0; i < this.size; i++)
            System.arraycopy(this.board[i], 0, b[i], 0, this.size);
        return b;
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
        Position start = raw.get(0), end = raw.get(raw.size() - 1);
        if (start.isOutside() || end.isOutside()) return false;
        String word = move.getWord();
        char[][] sBoard = this.getBoard();
        for (int i = 0; i < raw.size(); i++) { Position t = raw.get(i); sBoard[t.x][t.y] = word.charAt(i); }
        start = this.searcher(sBoard, start, dir == Move.direction.Horizontal ? 'L' : 'U');
        end = this.searcher(sBoard, end, dir == Move.direction.Horizontal ? 'R' : 'D');
        word = this.readWord(sBoard, start, end);
        if (!this.isValid(word)) return false;
        ArrayList<Position> positions = Move.getPositions(start, end);
        for (int i = 0; i < positions.size(); i++) {
            Position pos = positions.get(i);
            char tileAtPos = this.board[pos.x][pos.y], tile = word.charAt(i);
            if (tileAtPos == '-') {
                Position testStart = this.searcher(sBoard, pos, dir == Move.direction.Horizontal ? 'U' : 'L');
                Position testEnd = this.searcher(sBoard, pos, dir == Move.direction.Horizontal ? 'D' : 'R');
                if (!testStart.equals(testEnd) && !this.isValid(this.readWord(testStart, testEnd))) return false;
                int index = rack.contains(Character.toString(tile)) ?
                    rack.indexOf(tile) : rack.indexOf(Character.toLowerCase(tile));
                if (index < 0) return false;
                rack = Utils.removeAt(rack, index);
            }
            else if (tileAtPos != Character.toUpperCase(tile))
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
        ArrayList<Position> positions = move.getPositions();
        for (int i = 0; i < positions.size(); i++) {
            Position pos = positions.get(i);
            if (this.board[pos.x][pos.y] == '-') {
                char tile = word.charAt(i);
                int index = rack.contains(Character.toString(tile)) ?
                    rack.indexOf(tile) : rack.indexOf(Character.toLowerCase(tile));
                if (index >= 0) rack = Utils.removeAt(rack, index);
                this.board[pos.x][pos.y] = tile;
            }
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

