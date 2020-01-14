
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import org.junit.Test;

public class ScrabbLibTest 
{
    public ScrabbLibTest() {}
    
    @Test
    public void testScrabbLib() throws IOException, Exception
    {
        PrintStream out = new PrintStream(System.out, true, "UTF-8");
        out.println("Test 1.");
        ScrabbLib s = new ScrabbLib();
        s.initDictionary();
        long start = System.currentTimeMillis();
        List<String> list = s.generateWords("SEUZ-EQ").subList(0, 10);
        long timeElapsed = System.currentTimeMillis() - start;
        out.println("Generated words in: " + timeElapsed + " ms");
        list.stream().forEach(word -> { out.println(word); });
        
        out.println("\nTest 2.");
        Game g = new Game();
        Move m = new Move(new Position("8H"), Move.direction.Horizontal, "TEST");
        g.makeMove(m);
        g.displayBoard();
        g.displayPlayersInfo();
        Move m2 = new Move(new Position("6J"), Move.direction.Vertical, "TASTE");
        g.tryMakeMove(m2, "TASTEZX", "ACQS");
        g.displayBoard();
        g.displayPlayersInfo();
        
        out.println("\nTest 3.");
        start = System.currentTimeMillis();
        List<Move> solutions = s.generateMoves(g, "CVASZCA").subList(0, 20);
        timeElapsed = System.currentTimeMillis() - start;
        System.out.println("Generated words in: " + timeElapsed + " ms");
        solutions.stream().forEach(move -> {
            out.println(move.getPos() + ", Direction: " +
                move.getDir().name() + ", Word: "  + move.getWord()); });
    }
}

