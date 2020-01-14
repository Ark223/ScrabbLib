
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class ScrabbLibTest 
{
    public ScrabbLibTest() {}
    
    @Test
    public void testScrabbLib() throws IOException, Exception
    {
        ScrabbLib s = new ScrabbLib();
        s.initDictionary();
        long start = System.currentTimeMillis();
        List<String> list = s.generateWords("SEUZ-EQ").subList(0, 10);
        long timeElapsed = System.currentTimeMillis() - start;
        System.out.println("Generated words in: " + timeElapsed + " ms");
        PrintStream out = new PrintStream(System.out, true, "UTF-8");
        list.stream().forEach(word -> { out.println(word); });
        
        Game g = new Game();
        Move m = new Move(new Position("8H"), Move.direction.Horizontal, "TEST");
        g.makeMove(m);
        g.displayBoard();
        g.displayPlayersInfo();
        Move m2 = new Move(new Position("6J"), Move.direction.Vertical, "TASTE");
        g.tryMakeMove(m2, "TASTEZX", "ACQS");
        g.displayBoard();
        g.displayPlayersInfo();
        
        //ArrayList<Move> solutions = s.generateMoves(g, "CVASZCA");
        //out.println("Size: " + solutions.size());
        
    }
}

