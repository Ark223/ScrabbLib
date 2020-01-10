
import java.util.ArrayList;
import java.util.List;

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
    
    public static String removeAt(String s, int i) {
        return s.substring(0, i) + s.substring(i + 1);
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
}

