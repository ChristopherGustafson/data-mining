import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        final int numberOfRuns = 1;
        final int numberOfDocuments = 100;

        final int shingleLength = 5;
        // Initialize component classes with config values
        final int n = 100;
        MinHashing minHash = new MinHashing(n, 10000, 10079);
        LSH lsh = new LSH(20, 100, 0.5493);

        // Read CSV and extract abstracts
        System.out.println("Extracting abstracts from file...");
        List<String> abstracts = new ArrayList<String>();
        try{
            final CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
            final CSVReader reader = new CSVReaderBuilder(new FileReader("data/abstracts.csv")).withCSVParser(parser).build();
            String[] values = null;
            while ((values = reader.readNext()) != null && abstracts.size() < numberOfDocuments) {
                if(values.length >= 2) {
                    String val = values[1].replaceAll(
                            "[^a-zA-Z0-9 ]", "").toLowerCase();
                    abstracts.add(val);
                    System.out.println(abstracts.size()-1 + ": " + val);
                }
            }
        } catch (Exception e) {
            System.err.println("Couldn't find csv file");
        }
        long[] shingleExecTimes = new long[numberOfRuns];
        long[] jaccardExecTimes = new long[numberOfRuns];
        long[] minHashExecTimes = new long[numberOfRuns];
        long[] sigCompareExecTimes = new long[numberOfRuns];
        long[] lshExecTimes = new long[numberOfRuns];

        for(int run = 0; run < numberOfRuns; run++) {

            long shingleStartTime = System.nanoTime();
            // Create k-shingles for input abstracts
            LinkedHashSet<Integer>[] shingleSet = new LinkedHashSet[abstracts.size()];
            for(int i = 0; i < abstracts.size(); i++) {
                shingleSet[i] = Shingling.kShingles(shingleLength, abstracts.get(i));
            }

            long shingleEndTime = System.nanoTime();
            shingleExecTimes[run] = (shingleEndTime - shingleStartTime)/1000000;

            long jaccardStartTime = System.nanoTime();
            for(int i = 0; i < shingleSet.length; i++) {
                for(int j = 0; j < shingleSet.length; j++) {
                    if(shingleSet[i] != shingleSet[j]) {
                        double jaccardSimilarity = CompareSets.jaccardSimilarity(shingleSet[i], shingleSet[j]);
                        //if(jaccardSimilarity > 0.1) System.out.println(String.format("Jaccard Similarity between %d and %d is %f", i, j, jaccardSimilarity));
                    }
                }
            }
            long jaccardEndTime = System.nanoTime();
            jaccardExecTimes[run] = (jaccardEndTime - jaccardStartTime)/1000000;


            // Generate document signatures by means of minHashingR
            long minHashingStartTime = System.nanoTime();
            int[][] signatures = new int[abstracts.size()][n];
            for(int i = 0; i < shingleSet.length; i++) {
                signatures[i] = minHash.buildMinHash(shingleSet[i]);
            }
            long minHashingEndTime = System.nanoTime();
            minHashExecTimes[run] = (minHashingEndTime - minHashingStartTime)/1000000;

            // Compare minHash signatures to approximate Jaccard Similarity
            long signatureComparisonStartTime = System.nanoTime();
            for(int i = 0; i < shingleSet.length; i++) {
                for(int j = i+1; j < shingleSet.length; j++) {
                    double approxSimilarity = CompareSignatures.compareSignatures(signatures[i], signatures[j]);
                    //if(approxSimilarity > 0.1) System.out.println(String.format("Approximated Similarity between %d and %d is %f", i, j, approxSimilarity));
                }
            }
            long signatureComparisonEndTime = System.nanoTime();
            sigCompareExecTimes[run] = (signatureComparisonEndTime - signatureComparisonStartTime)/1000000;

            // Calculate LSH Candidate Pairs
            long lshStartTime = System.nanoTime();
            Set<IndexPair> similarPairs = lsh.findSimilarPairs(signatures);
            for(IndexPair pair: similarPairs) {
                System.out.println(pair);
            }
            long lshEndTime = System.nanoTime();
            lshExecTimes[run] = (lshEndTime - lshStartTime)/1000000;
        }

        Arrays.sort(shingleExecTimes);
        Arrays.sort(jaccardExecTimes);
        Arrays.sort(minHashExecTimes);
        Arrays.sort(sigCompareExecTimes);
        Arrays.sort(lshExecTimes);

        System.out.printf("Median execution times after %d runs%n", numberOfRuns);
        System.out.printf("Generating shingles: %d ms%n", shingleExecTimes[numberOfRuns/2]);
        System.out.printf("Jaccard Similarity calculation: %d ms%n", jaccardExecTimes[numberOfRuns/2]);
        System.out.printf("Generate MinHash signatures: %d ms%n", minHashExecTimes[numberOfRuns/2]);
        System.out.printf("Compare signatures: %d ms%n", sigCompareExecTimes[numberOfRuns/2]);
        System.out.printf("Find similar pairs with LSH: %d ms%n", lshExecTimes[numberOfRuns/2]);



    }
}
