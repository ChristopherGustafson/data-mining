import java.util.*;

public class Association {

    double s;
    double c;
    public Association(double s, double c) {
        this.s = s;
        this.c = c;
    }

    public void mineRules(Map<ArrayList<Integer>, Double> frequentItemSets) {

        // For every itemset
        for(ArrayList<Integer> itemset: frequentItemSets.keySet()){
            // For every possible subset length (excluding empty set and complete set)
            for(int length = 1; length < itemset.size(); length++) {
                // For every possible subset of that length
                for(ArrayList<Integer> subset: combinations(itemset, length)) {
                    ArrayList<Integer> otherSet = new ArrayList<>(itemset);
                    otherSet.removeAll(subset);
                    double confidence = support(itemset, frequentItemSets) / support(subset, frequentItemSets);
                    if(confidence > c) System.out.println("Confidence of " + subset + " -> " + otherSet + " = " + confidence);
                }
            }
        }
    }

    private Double support(ArrayList<Integer> itemset, Map<ArrayList<Integer>, Double> frequentItemSets){
        for (Map.Entry<ArrayList<Integer>, Double> entry : frequentItemSets.entrySet()) {
            if(itemset.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return 0.0;
    }

    private ArrayList<ArrayList<Integer>> combinations(ArrayList<Integer> input, int n) {
        ArrayList<ArrayList<Integer>> combinations = new ArrayList<>();
        for(int[] combIndexes: generateIndexes(input.size(), n)) {
            ArrayList<Integer> combination = new ArrayList<>();
            for(int i = 0; i < combIndexes.length; i++) {
                combination.add(input.get(combIndexes[i]));
            }
            combinations.add(combination);
        }
        return combinations;
    }

    private List<int[]> generateIndexes(int maxIndex, int n) {
        List<int[]> combinations = new ArrayList<>();
        int[] combination = new int[n];
        // initialize with lowest lexicographic combination
        for (int i = 0; i < n; i++) {
            combination[i] = i;
        }
        while (combination[n - 1] < maxIndex) {
            combinations.add(combination.clone());
            // generate next combination in lexicographic order
            int t = n - 1;
            while (t != 0 && combination[t] == maxIndex - n + t) {
                t--;
            }
            combination[t]++;
            for (int i = t + 1; i < n; i++) {
                combination[i] = combination[i - 1] + 1;
            }
        }
        return combinations;
    }

    // Helper function to fill frequentItemSets with all l2+ frequent itemsets, for testing purposes
    // as main program is very slow;
    public void generateInput(Map<ArrayList<Integer>, Double> frequentItemSets) {
        frequentItemSets.put(new ArrayList<>(Arrays.asList(217, 346)), 0.01336);
        frequentItemSets.put(new ArrayList<>(Arrays.asList(39, 825)), 0.01187);
        frequentItemSets.put(new ArrayList<>(Arrays.asList(227, 390)), 0.01049);
        frequentItemSets.put(new ArrayList<>(Arrays.asList(789, 829)), 0.01194);
        frequentItemSets.put(new ArrayList<>(Arrays.asList(704, 825)), 0.01102);
        frequentItemSets.put(new ArrayList<>(Arrays.asList(39, 704)), 0.01107);
        frequentItemSets.put(new ArrayList<>(Arrays.asList(368, 682)), 0.01193);
        frequentItemSets.put(new ArrayList<>(Arrays.asList(390, 722)), 0.01042);
        frequentItemSets.put(new ArrayList<>(Arrays.asList(368, 829)), 0.01194);
        frequentItemSets.put(new ArrayList<>(Arrays.asList(39, 704, 825)), 0.01035);
    }

}
