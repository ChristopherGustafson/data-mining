import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;

public class Shingling {

    // Return set of k-shingles for given input string
    public static LinkedHashSet<Integer> kShingles(int k, String input) {
        LinkedHashSet<Integer> shingles = new LinkedHashSet<Integer>();
        for(int i = 0; i < input.length()-k; i++) {
            shingles.add(input.substring(i, i+k).hashCode());
        }
        return shingles;
    }
}
