import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        long startTime = System.nanoTime();

        // Default config values
        // frequent item threshold
        double s = 0.01;
        Apriori apriori = new Apriori(s);
        double c = 0.5;
        Association assoc = new Association(s, c);

        // Read baskets from input file
        ArrayList<ArrayList<Integer>> baskets = new ArrayList<>();
        try {
            // Create buffered reader for input file
            FileReader fileReader = new FileReader(
                    "data/transactions.dat");
            BufferedReader reader = new BufferedReader(fileReader);

            // Read each line as a string, parse by space and add to baskets as list of integers.
            String basketString;
            while ((basketString=reader.readLine()) != null) {
                ArrayList<Integer> basket = (ArrayList<Integer>) Arrays.stream(basketString.split(" "))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                baskets.add(basket);
            }
            // Close reader
            reader.close();
        } catch(IOException e) {
            System.err.println("File not found");
        }

        // Data Structure to save all found frequent item sets
        Map<ArrayList<Integer>, Double> frequentItemSets = new HashMap<>();

        // Count all singletons - c1
        System.out.println("Generating c1....");
        HashMap<Integer, Integer> c1 = new HashMap<>();
        for(ArrayList<Integer> basket: baskets) {
            for(Integer item: basket) {
                if (c1.containsKey(item)) {
                    c1.put(item, c1.get(item) + 1);
                } else {
                    c1.put(item, 1);
                }
            }
        }
        System.out.println("Finished generating c1, found " + c1.size() + " entries");

        // Filter all singletons under support threshold
        System.out.println("Generating l1....");
        Map<Integer, Integer> l1 = c1.entrySet().stream()
                .filter(entry -> (entry.getValue().doubleValue()/baskets.size()) > s)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println("Finished generating l1, found " + l1.size() + " entries");

        // Add all l1 entries to the total frequent item sets
        for(Map.Entry<Integer, Integer> entry : l1.entrySet()) {
            frequentItemSets.put(new ArrayList<>(Arrays.asList(entry.getKey())), entry.getValue().doubleValue()/baskets.size());
        }

        // Generate c2 pairs, as required by general apriori function
        System.out.println("Generating c2 pairs...");
        ArrayList<ArrayList<Integer>> c2 = new ArrayList<>();
        for(Integer a: l1.keySet()) {
            for(Integer b: l1.keySet()) {
                if(a < b) {
                    c2.add(new ArrayList<>(Arrays.asList(a, b)));
                }
            }
        }
        System.out.println("Finished generating c2, found " + c2.size() + " entries");

        System.out.println("Generating l2...");
        Map<ArrayList<Integer>, Double> lk = apriori.filter(c2, baskets);
        // Save intermediate frequent sets for later computations
        frequentItemSets.putAll(lk);
        System.out.println("Finished generating l2 pairs: " + lk.size());
        for (Map.Entry<ArrayList<Integer>, Double> entry : lk.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " occurrences");
        }
        // Repeat the steps of generating candidate pairs and find frequent item sets increasing
        // the set size by 1 until no more frequent item sets are found.
        int stage = 3;
        while(!lk.isEmpty()) {
            System.out.println("Generating c" + stage + " pairs");
            ArrayList<ArrayList<Integer>> ck = apriori.generateCk(lk, l1);
            System.out.println("Finished generating c" + stage + ", found " + ck.size() + " entries");

            System.out.println("Generating l" + stage + " pairs");
            lk = apriori.filter(ck, baskets);
            // Save intermediate frequent sets for later computations
            frequentItemSets.putAll(lk);
            System.out.println("Finished generating l" + stage + ", found " + lk.size() + " entries");
            for (Map.Entry<ArrayList<Integer>, Double> entry : lk.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " occurrences");
            }
            stage++;
        }
        // Mine for association rules
        assoc.mineRules(frequentItemSets);

        long endTime = System.nanoTime();
        System.out.println("Program execution time: " + (endTime - startTime) / 1000000 + "ms");
    }
}
