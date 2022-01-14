import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Apriori {

    double s;
    public Apriori(double s) {
        this.s = s;
    }

    // Generates a set of candidate tuples, given a previous set of tuples and the initial list frequent singletons.
    // Each output tuples will have the length of the previous tuples + 1
    public ArrayList<ArrayList<Integer>> generateCk(Map<ArrayList<Integer>, Double> prevTuples, Map<Integer, Integer> singletons) {
        ArrayList<ArrayList<Integer>> ck = new ArrayList<>();

        // For every previous tuple
        for(ArrayList<Integer> tuple: prevTuples.keySet()) {
            // For every frequent singleton
            for(Integer single: singletons.keySet()) {
                // Check if the singleton is bigger than all entries of the tuple
                boolean bigger = true;
                for(Integer tupleItem: tuple) {
                    if(single <= tupleItem) {
                        bigger = false;
                        break;
                    }
                }
                // If the singleton is bigger, add the singleton to the tuple, and add this tuple
                // as a candidate tuple.
                if(bigger) {
                    ArrayList<Integer> newTuple = new ArrayList<>(tuple);
                    newTuple.add(single);
                    ck.add(newTuple);
                }
            }
        }
        return ck;
    }

    public Map<ArrayList<Integer>, Double> filter(ArrayList<ArrayList<Integer>> candidates, ArrayList<ArrayList<Integer>> baskets) {
        // HashMap<Tuple, Occurrences>
        HashMap<ArrayList<Integer>, Integer> ck = new HashMap<>();

        // For every candidate k-tuple
        for(ArrayList<Integer> kTuple: candidates) {
            // For every basket, check if it contains the tuple
            for(ArrayList<Integer> basket: baskets) {
                boolean containsTuple = true;
                for(Integer item: kTuple) {
                    if(!basket.contains(item)){
                        containsTuple = false;
                        break;
                    }
                }
                // If it does contain the tuple, increment its occurrence count
                if(containsTuple) {
                    if (ck.containsKey(kTuple)) {
                        ck.put(kTuple, ck.get(kTuple) + 1);
                    } else {
                        ck.put(kTuple, 1);
                    }
                }
            }
        }

        // Filter the tuples by the ones that occur more than the given support threshold s
        Map<ArrayList<Integer>, Double> filteredTuplesMap = ck.entrySet().stream()
                .filter(entry -> entry.getValue().doubleValue()/baskets.size() > s)
                .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue().doubleValue()/baskets.size()));
        //ArrayList<ArrayList<Integer>> filteredTuples = new ArrayList<>(filteredTuplesMap.keySet());

        return filteredTuplesMap;
    }

}
