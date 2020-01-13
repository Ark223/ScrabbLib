
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils
{
    public static List<String> cutByLength(List<String> list, int length) {
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (length == list.get(i).length()) {
                index = i; break;
            }
        }
        return new ArrayList<>(list.subList(0, index));
    }
    
    public static List<Character> extractor(String set) {
        List<Character> tiles = new ArrayList<>();
        Pattern p1 = Pattern.compile("\\d+"), p2 = Pattern.compile("\\D+");
        Matcher m1 = p1.matcher(set), m2 = p2.matcher(set);
        List<Integer> t1 = new ArrayList<>();
        List<Character> t2 = new ArrayList<>();
        while (m1.find()) t1.add(Integer.parseInt(m1.group()));
        while (m2.find()) t2.add(m2.group().charAt(0));
        for (int i = 0; i < t1.size(); i++)
            for (int j = 0; j < t1.get(i); j++)
                tiles.add(t2.get(i));
        return tiles;
    }
    
    public static boolean passWord(String word, String letters, int blanks, int i) {
        if (word.length() == 0 || letters.length() == 0)
            return true;
        char c = word.charAt(0);
        word = word.substring(1);
        int j = letters.indexOf(c);
        if (j == (-1))
        {
            if (blanks == i) return false;
            if (blanks > 0) i++;
        }
        else letters = removeAt(letters, j);
        return passWord(word, letters, blanks, i);
    }
    
    private static String replaceAt(String s, char c, int i) {
        return s.substring(0, i) + c + s.substring(i + 1);
    }
    
    public static String removeAt(String s, int i) {
        return s.substring(0, i) + s.substring(i + 1);
    }
}

