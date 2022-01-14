import java.util.LinkedHashSet;

public class CompareSets {

    public static double jaccardSimilarity(LinkedHashSet<Integer> a, LinkedHashSet<Integer> b) {

        double intersection = 0;
        LinkedHashSet<Integer> unionSet = new LinkedHashSet<>();

        for(int shingle : a) {
            unionSet.add(shingle);
            if(b.contains(shingle)) intersection++;
        }
        for(int shingle: b) {
            unionSet.add(shingle);
        }

        return  intersection/unionSet.size();
    }
}
