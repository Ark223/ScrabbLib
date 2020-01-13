
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScrabbLib
{
    private String language;
    public enum sortMode {Score, Length};
    public enum premiumSquare {None, DoubleLetter, TripleLetter, DoubleWord, TripleWord};
    private List<String> dictionary = new ArrayList<>();
    private final Map<Character, Integer> engValues = new HashMap<Character, Integer>() {{
        put('A', 1); put('B', 3); put('C', 3); put('D', 2); put('E', 1); put('F', 4); put('G', 2);
        put('H', 4); put('I', 1); put('J', 8); put('K', 5); put('L', 1); put('M', 3); put('N', 1);
        put('O', 1); put('P', 3); put('Q', 10); put('R', 1); put('S', 1); put('T', 1); put('U', 1);
        put('V', 4); put('W', 4); put('X', 8); put('Y', 4); put('Z', 10);
    }};
    private final Map<Character, Integer> plValues = new HashMap<Character, Integer>() {{
        put('A', 1); put('Ą', 5); put('B', 3); put('C', 2); put('Ć', 6); put('D', 2); put('E', 1);
        put('Ę', 5); put('F', 5); put('G', 3); put('H', 3); put('I', 8); put('J', 3); put('K', 2);
        put('L', 2); put('Ł', 3); put('M', 2); put('N', 1); put('Ń', 7); put('O', 1); put('Ó', 5);
        put('P', 2); put('R', 1); put('S', 1); put('Ś', 5); put('T', 2); put('U', 3); put('W', 1);
        put('Y', 2); put('Z', 1); put('Ź', 9); put('Ż', 5);
    }};
    public static final String bonuses = "T--d---T---d--T-D---t---t---D---D---d-d---D--" +
                                         "d--D---d---D--d----D-----D-----t---t---t---t-" +
                                         "--d---d-d---d--T--d-------d--T--d---d-d---d--" +
                                         "-t---t---t---t-----D-----D----d--D---d---D--d" +
                                         "--D---d-d---D---D---t---t---D-T--d---T---d--T";
    
    public ScrabbLib() {
        this("EN");
    }
    
    public ScrabbLib(String lang) {
        this.language = lang;
    }
    
    // Public
    
    public void initDictionary() throws IOException {
        try {
            File f = new File("dictionaries/" + (this.language.equals("EN") ? "en.txt" : "pl.txt"));
            this.dictionary = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
            Collections.sort(this.dictionary, (String a, String b) -> a.length() - b.length());
        } catch (Exception ex) {
            throw new IOException("The dictionary file could not be read!");
        }
    }
    
    public int score(String word) {
        int total = 0;
        for (char c : word.toUpperCase().toCharArray())
            total += this.language.equals("EN") ?
                this.engValues.get(c) : this.plValues.get(c);
        return total;
    }
    
    public boolean isValid(String word) {
        return this.dictionary.stream().anyMatch(s -> s.equals(word.toUpperCase()));
    }
    
    public List<String> generateWords(String letters) {
        return this.generateWords(letters, sortMode.Score);
    }
    
    public List<String> generateWords(String letters, sortMode sort) {
        int blanks = (int)letters.chars().filter(c -> c == '-').count();
        List<String> results = Utils.cutByLength(dictionary, letters.length() + 1).parallelStream()
            .filter(word -> Utils.passWord(word, letters, blanks, 0)).collect(Collectors.toList());
        Collections.sort(results, (String a, String b) -> sort == sortMode.Length
            ? b.length() - a.length() : Integer.compare(this.score(b), this.score(a)));
        return results;
    }
    
    public ArrayList<Move> generateMoves(Game game) {
        return this.generateMoves(game, game.getPlayersRack(), sortMode.Score);
    }
    
    public ArrayList<Move> generateMoves(Game game, sortMode sort) {
        return this.generateMoves(game, game.getPlayersRack(), sort);
    }
    
    public ArrayList<Move> generateMoves(Game game, String letters) {
        return this.generateMoves(game, letters, sortMode.Score);
    }
    
    public ArrayList<Move> generateMoves(Game game, String letters, sortMode sort) {
        ArrayList<Move> solutions = new ArrayList<>();
        List<String> empty = this.generateWords(letters, sortMode.Length);
        char[][] board = game.getBoard();
        for (int M = 0; M < 2; M++) {
            for (int T = 0; T < 15; T++) {
                String extra = "";
                for (int i = 0; i < 15; i++) {
                    char c = board[M == 1 ? T : i][M == 0 ? T : i];
                    if (c != '-') extra += c;
                }
                List<String> candidates = extra.equals("") ? empty :
                    this.generateWords(letters + extra, sortMode.Length);
                for (String word : candidates) {
                    for (int i = 0; i < 15 - word.length() + 1; i++) {
                        Move m = new Move(new Position(M == 1 ? T : i, M == 0 ? T : i),
                            M == 0 ? Move.direction.Horizontal : Move.direction.Vertical, word);
                        if (game.isValid(m, letters)) solutions.add(m);
                    }
                }
            }
        }
        return solutions;
    }
}

